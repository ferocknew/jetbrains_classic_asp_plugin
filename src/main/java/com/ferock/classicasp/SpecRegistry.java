package com.ferock.classicasp;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 读取并缓存 src/main/resources/keywords.yaml 的轻量注册器。
 * 失败时回退到内置的 VBScriptKeywords 数据。
 */
public final class SpecRegistry {

    private static volatile boolean loaded = false;

    private static Map<String, String> caseMap = new HashMap<>();                // 小写 -> 正确大小写
    private static Map<String, Set<String>> objectToMethods = new HashMap<>();   // 对象名(小写) -> 方法集合(小写)
    private static Map<String, Map<String, String>> objectMethodCase = new HashMap<>(); // 对象名(小写) -> (方法小写 -> 方法正确大小写)
    private static Set<String> sysFunctions = new HashSet<>();                   // 内置函数名集合(小写)
    private static Map<String, String> sysFunctionCase = new HashMap<>();        // 内置函数 小写->正确大小写
    private static Set<String> keywords = new HashSet<>();                        // YAML 中声明的关键词（小写聚合）

    // YAML: formatting
    private static List<String> operatorSymbolsWithSpaces = new ArrayList<>();   // 需要空格的符号运算符（字符串形式）
    private static Set<String> keywordOpsWithSpaces = new HashSet<>();           // 需要空格的关键字运算符（小写）

    private SpecRegistry() {}

    public static Map<String, String> getCaseMap() { ensureLoaded(); return caseMap; }
    public static Map<String, Set<String>> getObjectToMethods() { ensureLoaded(); return objectToMethods; }
    public static Map<String, Map<String, String>> getObjectMethodCase() { ensureLoaded(); return objectMethodCase; }
    public static Set<String> getSysFunctions() { ensureLoaded(); return sysFunctions; }
    public static Map<String, String> getSysFunctionCase() { ensureLoaded(); return sysFunctionCase; }
    public static List<String> getOperatorSymbolsWithSpaces() { ensureLoaded(); return operatorSymbolsWithSpaces; }
    public static Set<String> getKeywordOpsWithSpaces() { ensureLoaded(); return keywordOpsWithSpaces; }
    public static Set<String> getKeywords() { ensureLoaded(); return keywords; }

    private static synchronized void ensureLoaded() {
        if (loaded) return;
        try (InputStream in = SpecRegistry.class.getClassLoader().getResourceAsStream("keywords.yaml")) {
            if (in == null) { fallbackToBuiltin("[SpecRegistry] keywords.yaml not found, fallback to built-in tables."); return; }
            Yaml yaml = new Yaml();
            Object data = yaml.load(in);
            if (!(data instanceof Map)) { fallbackToBuiltin("[SpecRegistry] YAML root is not a map, fallback."); return; }
            Map<?,?> root = (Map<?,?>) data;

            // case_map
            Object cm = root.get("case_map");
            if (cm instanceof Map) {
                Map<?,?> m = (Map<?,?>) cm;
                for (Map.Entry<?,?> e : m.entrySet()) {
                    if (e.getKey() != null && e.getValue() != null) {
                        caseMap.put(String.valueOf(e.getKey()).toLowerCase(), String.valueOf(e.getValue()));
                    }
                }
            }

            // objects.methods
            Object objects = root.get("objects");
            if (objects instanceof Map) {
                Map<?,?> objMap = (Map<?,?>) objects;
                for (Map.Entry<?,?> e : objMap.entrySet()) {
                    String objectName = String.valueOf(e.getKey());
                    Object def = e.getValue();
                    if (def instanceof Map) {
                        Object methods = ((Map<?,?>) def).get("methods");
                        Set<String> methodSet = new HashSet<>();
                        Map<String, String> methodCase = new HashMap<>();
                        if (methods instanceof List) {
                            for (Object m : (List<?>) methods) {
                                if (m != null) {
                                    String proper = String.valueOf(m);
                                    String lower = proper.toLowerCase();
                                    methodSet.add(lower);
                                    methodCase.put(lower, proper);
                                }
                            }
                        }
                        objectToMethods.put(objectName.toLowerCase(), methodSet);
                        objectMethodCase.put(objectName.toLowerCase(), methodCase);
                    }
                }
            }

            // keywords: 聚合所有类别
            Object kroot = root.get("keywords");
            if (kroot instanceof Map) {
                Map<?,?> km = (Map<?,?>) kroot;
                for (Object v : km.values()) {
                    if (v instanceof List) {
                        for (Object kw : (List<?>) v) {
                            if (kw != null) keywords.add(String.valueOf(kw).toLowerCase());
                        }
                    }
                }
            }

            // sys_functions
            Object sf = root.get("sys_functions");
            if (sf instanceof List) {
                for (Object fn : (List<?>) sf) {
                    if (fn != null) {
                        String proper = String.valueOf(fn);
                        String lower = proper.toLowerCase();
                        sysFunctions.add(lower);
                        sysFunctionCase.put(lower, proper);
                    }
                }
            }

            // formatting: operators_with_spaces / keywords_with_spaces
            Object formatting = root.get("formatting");
            if (formatting instanceof Map) {
                Map<?,?> fmt = (Map<?,?>) formatting;
                // collect operators map for symbols
                Map<String, Object> operators = new HashMap<>();
                Object ops = root.get("operators");
                if (ops instanceof Map) {
                    for (Map.Entry<?,?> e : ((Map<?,?>) ops).entrySet()) {
                        operators.put(String.valueOf(e.getKey()), e.getValue());
                    }
                }
                // operators_with_spaces: list of group keys
                Object ows = fmt.get("operators_with_spaces");
                if (ows instanceof List) {
                    for (Object g : (List<?>) ows) {
                        String group = String.valueOf(g);
                        Object val = operators.get(group);
                        if (val instanceof Map) {
                            // map of TOKEN->symbol
                            for (Object sym : ((Map<?,?>) val).values()) {
                                if (sym != null) operatorSymbolsWithSpaces.add(String.valueOf(sym));
                            }
                        } else if (val instanceof List) {
                            // list of keywords (ignore here)
                        }
                    }
                }
                // keywords_with_spaces
                Object kws = fmt.get("keywords_with_spaces");
                if (kws instanceof List) {
                    for (Object kw : (List<?>) kws) {
                        if (kw != null) keywordOpsWithSpaces.add(String.valueOf(kw).toLowerCase());
                    }
                }
            }

            loaded = true;
            System.out.println("[ASP][Spec] keywords.yaml loaded: caseMap=" + caseMap.size() + ", objects=" + objectToMethods.size() + ", sysFns=" + sysFunctions.size());
        } catch (Throwable t) {
            fallbackToBuiltin("[SpecRegistry] load error, fallback: " + t.getMessage());
        }
    }

    private static void fallbackToBuiltin(String reason) {
        System.out.println(reason);
        try { caseMap = VBScriptKeywords.getCaseMap(); } catch (Throwable ignored) { caseMap = Collections.emptyMap(); }
        objectToMethods = new HashMap<>();
        objectMethodCase = new HashMap<>();
        sysFunctions = new HashSet<>();
        sysFunctionCase = new HashMap<>();
        operatorSymbolsWithSpaces = new ArrayList<>();
        keywordOpsWithSpaces = new HashSet<>();
        loaded = true;
    }
}

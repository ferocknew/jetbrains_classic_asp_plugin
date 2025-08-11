package com.ferock.classicasp.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiFile;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import com.ferock.classicasp.SpecRegistry;
import com.ferock.classicasp.ASPSectionDetector;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassicASPCompletionContributor extends CompletionContributor {

    public ClassicASPCompletionContributor() {
        // 不限定语言，避免无解析树/语言标记时无法触发
        extend(CompletionType.BASIC,
               PlatformPatterns.psiElement(),
               new ClassicASPCompletionProvider());
    }

    private static class ClassicASPCompletionProvider extends CompletionProvider<CompletionParameters> {
        @Override
        protected void addCompletions(@NotNull CompletionParameters parameters,
                                      @NotNull ProcessingContext context,
                                      @NotNull CompletionResultSet result) {
            PsiFile file = parameters.getOriginalFile();
            int offset = parameters.getOffset();
            String text = file.getText();
            String fileName = file.getName();

            log("[ASP][CC] enter file=" + fileName + ", offset=" + offset
                    + ", fileType=" + (file.getFileType() == null ? "null" : file.getFileType().getName())
                    + ", lang=" + (file.getLanguage() == null ? "null" : file.getLanguage().getID())
            );

            // 仅对 .asp 文件生效
            if (fileName == null || !fileName.toLowerCase(Locale.ROOT).endsWith(".asp")) {
                log("[ASP][CC] skip: not .asp file");
                return;
            }

            boolean inAsp = ASPSectionDetector.containsASPCode(text) && ASPSectionDetector.isInASPSection(text, offset);
            if (!inAsp) {
                log("[ASP][CC] skip: not in ASP section");
                return;
            }

            String objectCtx = detectObjectBeforeDot(text, offset);
            List<LookupElement> suggestions = new ArrayList<>();

            if (objectCtx != null) {
                Map<String, Map<String, String>> omc = SpecRegistry.getObjectMethodCase();
                Map<String, String> methodCase = omc.get(objectCtx.toLowerCase(Locale.ROOT));
                if (methodCase != null && !methodCase.isEmpty()) {
                    for (Map.Entry<String, String> e : methodCase.entrySet()) {
                        String proper = e.getValue();
                        suggestions.add(withParens(LookupElementBuilder.create(proper)
                                .withTypeText("方法")));
                    }
                }
                log("[ASP][CC] ctx object=" + objectCtx + ", proposals=" + suggestions.size());
            } else {
                // 先加入本页定义的变量/方法/属性
                LocalSymbols local = collectLocalSymbols(text);
                int localAdded = 0;
                for (String v : local.variables) {
                    suggestions.add(LookupElementBuilder.create(v).withTypeText("变量(本页)"));
                    localAdded++;
                }
                for (String f : local.functions) {
                    suggestions.add(withParens(LookupElementBuilder.create(f).withTypeText("方法(本页)")));
                    localAdded++;
                }
                for (String p : local.properties) {
                    suggestions.add(LookupElementBuilder.create(p).withTypeText("属性(本页)"));
                    localAdded++;
                }
                log("[ASP][CC] 本页符号: 变量=" + local.variables.size() + ", 方法=" + local.functions.size() + ", 属性=" + local.properties.size() + ", 已加入=" + localAdded);

                // YAML 驱动（关键词/内置函数/对象）
                Set<String> seen = new HashSet<>();
                // 避免与本地符号重名的重复（大小写不敏感）
                for (String v : local.variables) seen.add(v.toLowerCase(Locale.ROOT));
                for (String f : local.functions) seen.add(f.toLowerCase(Locale.ROOT));
                for (String p : local.properties) seen.add(p.toLowerCase(Locale.ROOT));

                Map<String, String> caseMap = SpecRegistry.getCaseMap();
                int kwCount = 0;
                for (Map.Entry<String, String> e : caseMap.entrySet()) {
                    String proper = e.getValue();
                    String key = proper.toLowerCase(Locale.ROOT);
                    if (seen.add(key)) {
                        suggestions.add(LookupElementBuilder.create(proper).withTypeText("关键字/函数"));
                        kwCount++;
                    }
                }

                Map<String, String> sysFnCase = SpecRegistry.getSysFunctionCase();
                int fnCount = 0;
                for (Map.Entry<String, String> e : sysFnCase.entrySet()) {
                    String proper = e.getValue();
                    String key = proper.toLowerCase(Locale.ROOT);
                    if (seen.add(key)) {
                        suggestions.add(withParens(LookupElementBuilder.create(proper).withTypeText("内置函数")));
                        fnCount++;
                    }
                }

                Map<String, Map<String, String>> omc = SpecRegistry.getObjectMethodCase();
                int objCount = 0;
                for (String objLower : omc.keySet()) {
                    if (objLower == null || objLower.isEmpty()) continue;
                    String properObj = Character.toUpperCase(objLower.charAt(0)) + objLower.substring(1);
                    if (seen.add("obj:" + objLower)) {
                        suggestions.add(LookupElementBuilder.create(properObj).withTypeText("对象"));
                        objCount++;
                    }
                }

                log("[ASP][CC] kw/sysfn/obj proposals=" + kwCount + "/" + fnCount + "/" + objCount + ", total=" + suggestions.size());
            }

            suggestions.sort(Comparator.comparing(LookupElement::getLookupString, String.CASE_INSENSITIVE_ORDER));

            String prefix = extractTypedPrefix(text, offset);
            CompletionResultSet base = result.caseInsensitive();
            CompletionResultSet filtered = prefix.isEmpty() ? base : base.withPrefixMatcher(prefix);
            filtered.addAllElements(suggestions);

            log("[ASP][CC] done prefix='" + prefix + "', proposals=" + suggestions.size());
        }

        private String detectObjectBeforeDot(String text, int offset) {
            if (text == null || offset <= 0) return null;
            int start = Math.max(0, offset - 64);
            String left = text.substring(start, Math.min(text.length(), offset));
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("([A-Za-z][A-Za-z0-9_]*)\\s*\\.\\s*[$A-Za-z0-9_]*$").matcher(left);
            if (m.find()) {
                return m.group(1);
            }
            return null;
        }

        private String extractTypedPrefix(String text, int offset) {
            if (text == null || offset <= 0) return "";
            int i = offset - 1;
            while (i >= 0) {
                char c = text.charAt(i);
                if (Character.isLetterOrDigit(c) || c == '_') {
                    i--;
                } else break;
            }
            return text.substring(i + 1, offset);
        }

        private static void log(String msg) {
            // 仅输出到控制台，避免文件写入
            System.out.println(msg);
        }

        private static LookupElementBuilder withParens(LookupElementBuilder builder) {
            return builder.withInsertHandler((context, item) -> {
                int tail = context.getTailOffset();
                CharSequence seq = context.getDocument().getCharsSequence();
                if (!(tail < seq.length() && seq.charAt(tail) == '(')) {
                    context.getDocument().insertString(tail, "()");
                    context.getEditor().getCaretModel().moveToOffset(tail + 1);
                }
            });
        }

        // 收集当前页面（仅 ASP 区域）定义的变量、方法、属性
        private LocalSymbols collectLocalSymbols(String text) {
            LocalSymbols ls = new LocalSymbols();
            int[][] sections = ASPSectionDetector.getASPSections(text);
            if (sections == null || sections.length == 0) return ls;

            // 正则
            Pattern dimLine = Pattern.compile("(?im)\\bDim\\b\\s+([^'\\r\\n]+)");
            Pattern classField = Pattern.compile("(?im)\\b(Public|Private)\\b\\s+([A-Za-z_][A-Za-z0-9_]*)\\b(?!\\s*\\()");
            Pattern funcDecl = Pattern.compile("(?im)\\b(Function|Sub)\\s+([A-Za-z_][A-Za-z0-9_]*)\\b");
            Pattern propDecl = Pattern.compile("(?im)\\bProperty\\s+(Get|Let|Set)\\s+([A-Za-z_][A-Za-z0-9_]*)\\b");

            for (int[] sec : sections) {
                int start = Math.max(0, sec[0]);
                int end = Math.min(text.length(), sec[1]);
                if (start >= end) continue;
                String asp = text.substring(start, end);

                // Dim a, b, c
                Matcher mDim = dimLine.matcher(asp);
                while (mDim.find()) {
                    String list = mDim.group(1);
                    if (list == null) continue;
                    for (String raw : list.split(",")) {
                        String name = raw.trim();
                        if (name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
                            ls.variables.add(name);
                        }
                    }
                }
                // Public/Private foo  (避免 Public Function ... 被匹配，通过(?!\s*\() 排除方法)
                Matcher mField = classField.matcher(asp);
                while (mField.find()) {
                    String name = mField.group(2);
                    if (name != null) ls.variables.add(name);
                }
                // Function/Sub 名称
                Matcher mFn = funcDecl.matcher(asp);
                while (mFn.find()) {
                    String name = mFn.group(2);
                    if (name != null) ls.functions.add(name);
                }
                // Property Get/Let/Set 名称
                Matcher mProp = propDecl.matcher(asp);
                while (mProp.find()) {
                    String name = mProp.group(2);
                    if (name != null) ls.properties.add(name);
                }
            }
            return ls;
        }

        private static class LocalSymbols {
            final Set<String> variables = new LinkedHashSet<>();
            final Set<String> functions = new LinkedHashSet<>();
            final Set<String> properties = new LinkedHashSet<>();
        }
    }
}
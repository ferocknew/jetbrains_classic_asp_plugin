package com.ferock.classicasp.formatting;

import com.ferock.classicasp.ClassicASPLanguage;
import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;
import com.intellij.formatting.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;

/**
 * Classic ASP 代码格式化模型构建器
 * 参考官方示例实现，提供简洁的格式化功能
 */
public class ClassicASPFormattingModelBuilder implements FormattingModelBuilder {

    /**
     * 创建间距构建器 - 根据用户需求实现
     */
    private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
        return new SpacingBuilder(settings, ClassicASPLanguage.INSTANCE)
                // 等号左右有空格 - 规则1，但不换行
                .around(ClassicASPTokenTypes.EQUALS)
                .spacing(1, 1, 0, false, 1)
                // 所有比较操作符左右有空格
                .around(ClassicASPTokenTypes.NEQ)
                .spacing(1, 1, 0, false, 1)
                .around(ClassicASPTokenTypes.LESS_EQUAL)
                .spacing(1, 1, 0, false, 1)
                .around(ClassicASPTokenTypes.GREATER_EQUAL)
                .spacing(1, 1, 0, false, 1)
                .around(ClassicASPTokenTypes.IS)
                .spacing(1, 1, 0, false, 1)
                // 其他操作符间距
                .around(ClassicASPTokenTypes.PLUS)
                .spacing(1, 1, 0, false, 1)
                .around(ClassicASPTokenTypes.MINUS)
                .spacing(1, 1, 0, false, 1)
                // 括号间距 - 不添加空格
                .around(ClassicASPTokenTypes.LPAREN)
                .none()
                .around(ClassicASPTokenTypes.RPAREN)
                .none()
                // 注释间距 - 强制换行
                .after(ClassicASPTokenTypes.COMMENT)
                .lineBreakInCode()
                // 空行处理现在由BlankLineProcessor专门处理
                // Then 前只有一个空格
                .before(ClassicASPTokenTypes.THEN)
                .spacing(1, 1, 0, false, 1)
                // End If 不换行
                .around(ClassicASPTokenTypes.END_IF)
                .none();
    }

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        // System.out.println("🔍 [FORMAT_MODEL] 开始创建格式化模型");
        try {
            final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();

            // System.out.println("🔍 [FORMAT_MODEL] 创建ClassicASPBlock");
            ClassicASPBlock block = new ClassicASPBlock(formattingContext.getNode(),
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    createSpaceBuilder(codeStyleSettings), false);

            // System.out.println("🔍 [FORMAT_MODEL] 创建FormattingModel");
            return FormattingModelProvider
                    .createFormattingModelForPsiFile(formattingContext.getContainingFile(),
                            block,
                            codeStyleSettings);
        } catch (Exception e) {
            // 如果格式化模型创建失败，返回一个简单的模型
            System.err.println("ClassicASPFormattingModelBuilder error: " + e.getMessage());
            e.printStackTrace();
            return FormattingModelProvider.createFormattingModelForPsiFile(
                    formattingContext.getContainingFile(),
                    new ClassicASPBlock(formattingContext.getNode(),
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            new SpacingBuilder(formattingContext.getCodeStyleSettings(), ClassicASPLanguage.INSTANCE),
                            false),
                    formattingContext.getCodeStyleSettings());
        }
    }

    /**
     * 将关键词转换为大驼峰格式
     * 例如：end if -> End If
     */
    public static String normalizeKeyword(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }

        String lowerWord = word.toLowerCase();

        // 处理复合关键词
        if (lowerWord.equals("end if")) return "End If";
        if (lowerWord.equals("end function")) return "End Function";
        if (lowerWord.equals("end sub")) return "End Sub";
        if (lowerWord.equals("end while")) return "End While";
        if (lowerWord.equals("end do")) return "End Do";
        if (lowerWord.equals("end select")) return "End Select";
        if (lowerWord.equals("end property")) return "End Property";

        // 检查是否是关键词
        switch (lowerWord) {
            case "if":
            case "then":
            case "else":
            case "end":
            case "for":
            case "next":
            case "while":
            case "wend":
            case "do":
            case "loop":
            case "function":
            case "sub":
            case "dim":
            case "set":
            case "select":
            case "case":
            case "property":
            case "each":
            case "in":
            case "response":
            case "request":
            case "server":
            case "session":
            case "application":
            case "array":
                // 转换为大驼峰格式
                return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            default:
                return word; // 非关键词保持原样
        }
    }
}
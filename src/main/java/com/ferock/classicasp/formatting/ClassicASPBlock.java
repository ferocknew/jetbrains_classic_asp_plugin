package com.ferock.classicasp.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.ferock.classicasp.highlighter.ClassicASPTokenTypes;
import com.ferock.classicasp.ASPSectionDetector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Classic ASP 格式化块
 * 参考官方示例实现，提供简洁的格式化功能
 */
public class ClassicASPBlock extends AbstractBlock {

    private final SpacingBuilder spacingBuilder;

    private final boolean isInsideControlStructure;

    protected ClassicASPBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment,
                             SpacingBuilder spacingBuilder, boolean isInsideControlStructure) {
        super(node, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.isInsideControlStructure = isInsideControlStructure;

    }

        @Override
    protected List<Block> buildChildren() {
        return com.ferock.classicasp.SafetyLimits.safeExecute(() -> {
            List<Block> blocks = new ArrayList<>();
            ASTNode child = myNode.getFirstChildNode();
            boolean currentIsControlStructure = isControlStructure(myNode.getElementType());



            // 安全限制：防止死循环
            int iterations = 0;

            while (child != null && iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
                iterations++;

                if (child.getElementType() != TokenType.WHITE_SPACE) {
                    // 如果当前节点是控制结构，其子元素需要缩进
                    // 如果当前节点已经在控制结构内，继续传递这个状态
                    boolean childShouldBeIndented = isInsideControlStructure;

                    // 如果当前节点是控制结构，那么它的子节点应该缩进
                    if (currentIsControlStructure) {
                        childShouldBeIndented = true;
                    }



                    Block block = new ClassicASPBlock(child, Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(), spacingBuilder, childShouldBeIndented);
                    blocks.add(block);
                }
                child = child.getTreeNext();
            }

            com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPBlock.buildChildren");
            return blocks;
        }, "ClassicASPBlock.buildChildren", new ArrayList<>());
    }

    private boolean isControlStructure(IElementType type) {
        return type == ClassicASPTokenTypes.IF ||
               type == ClassicASPTokenTypes.FOR ||
               type == ClassicASPTokenTypes.WHILE ||
               type == ClassicASPTokenTypes.DO ||
               type == ClassicASPTokenTypes.FUNCTION ||
               type == ClassicASPTokenTypes.SUB ||
               type == ClassicASPTokenTypes.SELECT ||
               type == ClassicASPTokenTypes.PROPERTY ||
               type == ClassicASPTokenTypes.CLASS ||
               type == ClassicASPTokenTypes.PUBLIC ||
               type == ClassicASPTokenTypes.PRIVATE;
    }

    @Override
    public Indent getIndent() {
        // 只对ASP代码区域进行格式化，HTML区域不格式化
        if (!isInASPSection()) {
            return Indent.getNoneIndent();
        }

        // ASP 标签不缩进
        if (myNode.getElementType() == ClassicASPTokenTypes.ASP_START ||
            myNode.getElementType() == ClassicASPTokenTypes.ASP_END) {
            return Indent.getNoneIndent();
        }

        // 控制结构开始关键字不缩进
        if (isControlStructure(myNode.getElementType())) {
            return Indent.getNoneIndent();
        }

        // 控制结构结束关键字不缩进
        if (isControlStructureEnd(myNode.getElementType())) {
            return Indent.getNoneIndent();
        }

        // Case 语句特殊处理：缩进一级，但内容需要缩进两级
        if (myNode.getElementType() == ClassicASPTokenTypes.CASE) {
            return Indent.getNormalIndent();
        }

        // 如果在控制结构内，普通语句需要缩进
        if (isInsideControlStructure) {
            // 检查是否在Case语句内部（需要双重缩进）
            if (isInsideCaseStatement()) {
                // 使用8个空格来实现双重缩进（假设正常缩进是4个空格）
                return Indent.getSpaceIndent(8);
            }
            // 使用4个空格作为标准缩进
            return Indent.getSpaceIndent(4);
        }

        // 对于新行，不添加缩进
        return Indent.getNoneIndent();
    }

    private boolean isControlStructureEnd(IElementType type) {
        return type == ClassicASPTokenTypes.END_IF ||
               type == ClassicASPTokenTypes.NEXT ||
               type == ClassicASPTokenTypes.END_FUNCTION ||
               type == ClassicASPTokenTypes.END_SUB ||
               type == ClassicASPTokenTypes.END_WHILE ||
               type == ClassicASPTokenTypes.END_DO ||
               type == ClassicASPTokenTypes.END_LOOP ||
               type == ClassicASPTokenTypes.END_FOR ||
               type == ClassicASPTokenTypes.END_SELECT ||
               type == ClassicASPTokenTypes.END_PROPERTY ||
               type == ClassicASPTokenTypes.END_CLASS ||
               type == ClassicASPTokenTypes.WEND ||
               type == ClassicASPTokenTypes.LOOP;  // Do-Loop 结构的结束
    }

        /**
     * 检查当前节点是否在ASP代码区域内
     * 使用公共的ASPSectionDetector
     * @return 是否在ASP区域内
     */
    private boolean isInASPSection() {
        // 获取当前节点的位置信息
        int currentNodeStart = myNode.getStartOffset();

        // 获取文件内容
        CharSequence fileContent = myNode.getTreeParent() != null ?
            myNode.getTreeParent().getText() : myNode.getText();

        // 使用公共的ASP区域检测器
        boolean inASPSection = ASPSectionDetector.isInASPSection(fileContent.toString(), currentNodeStart);

                return inASPSection;
    }

                private boolean isInsideCaseStatement() {
        // 更简单的方法：检查当前节点是否在Case和下一个Case/End Select之间
        ASTNode current = myNode;

        // 安全限制：防止死循环
        int iterations = 0;

        // 向前查找，直到找到Case语句
        ASTNode searchNode = current.getTreePrev();
        while (searchNode != null && iterations < com.ferock.classicasp.SafetyLimits.MAX_LOOP_ITERATIONS) {
            iterations++;
            IElementType type = searchNode.getElementType();

            if (type == ClassicASPTokenTypes.CASE) {
                // 找到了Case，现在检查我们是否在这个Case的内容中
                return true;
            }

            // 如果遇到Select或End Select，说明不在Case内部
            if (type == ClassicASPTokenTypes.SELECT ||
                type == ClassicASPTokenTypes.END_SELECT) {
                return false;
            }

            searchNode = searchNode.getTreePrev();
        }

        com.ferock.classicasp.SafetyLimits.checkLoopLimit(iterations, "ClassicASPBlock.isInsideCaseStatement");

        return false;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
        return spacingBuilder.getSpacing(this, child1, child2);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
package com.ferock.classicasp;

import com.ferock.classicasp.lexer.ClassicASPLexerAdapter;
import com.ferock.classicasp.parser.ClassicASPParser;
import com.ferock.classicasp.psi.ClassicASTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class ClassicASPParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(ClassicASPLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new ClassicASPLexerAdapter();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new ClassicASPParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        // 告知解析器：将 ClassicASTypes.WHITE_SPACE 视为可跳过的空白
        return TokenSet.create(ClassicASTypes.WHITE_SPACE);
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.create(ClassicASTypes.STRING_LITERAL);
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return ClassicASTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new ClassicASPFile(viewProvider);
    }
}

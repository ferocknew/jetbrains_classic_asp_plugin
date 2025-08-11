package com.ferock.classicasp.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ClassicASTokenType extends IElementType {
  public ClassicASTokenType(@NotNull @NonNls String debugName) {
    super(debugName, com.ferock.classicasp.ClassicASPLanguage.INSTANCE);
  }
}

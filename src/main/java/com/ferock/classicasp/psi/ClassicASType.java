package com.ferock.classicasp.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ClassicASType extends IElementType {
  public ClassicASType(@NotNull @NonNls String debugName) {
    super(debugName, com.ferock.classicasp.ClassicASPLanguage.INSTANCE);
  }
}

package com.reason.lang.core.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.reason.lang.RmlTypes;
import com.reason.lang.core.psi.PsiModuleName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiModuleNameImpl extends ASTWrapperPsiElement implements PsiModuleName {
    public PsiModuleNameImpl(@NotNull ASTNode node) {
        super(node);
    }

    //region NamedElement
    @Override
    public String getName() {
        PsiElement nameElement = getNameIdentifier();
        return nameElement == null ? "" : nameElement.getText();
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(RmlTypes.VALUE_NAME);
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        return this; // Use PsiModuleReference.handleElementRename()
    }
    //endregion

    @Override
    public PsiReference getReference() {
        return new PsiModuleReference(this);
    }

    @Override
    public String toString() {
        return "Module.name(" + getName() + ")";
    }
}
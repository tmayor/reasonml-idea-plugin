package com.reason.lang.core.psi.impl;

import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.xml.model.gotosymbol.GoToSymbolProvider;
import com.reason.lang.core.ORUtil;
import com.reason.lang.core.psi.PsiConstraints;
import com.reason.lang.core.psi.PsiFunctorCall;
import com.reason.lang.core.psi.PsiInclude;
import com.reason.lang.core.type.ORTypes;
import icons.ORIcons;

public class PsiIncludeImpl extends PsiToken<ORTypes> implements PsiInclude {

    //region Constructors
    public PsiIncludeImpl(@NotNull ORTypes types, @NotNull ASTNode node) {
        super(types, node);
    }
    //endregion

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        throw new IncorrectOperationException("Not implemented");
    }

    @NotNull
    @Override
    public String getPath() {
        PsiElement firstChild = PsiTreeUtil.skipWhitespacesForward(getFirstChild());
        if (firstChild instanceof PsiFunctorCall) {
            return ((PsiFunctorCall) firstChild).getFunctorName();
        }
        return firstChild == null ? "" : ORUtil.getTextUntilClass(firstChild, PsiConstraints.class);
    }

    @NotNull
    @Override
    public String getQualifiedName() {
        return getPath();
    }

    @Override
    public boolean useFunctor() {
        PsiElement firstChild = PsiTreeUtil.skipWhitespacesForward(getFirstChild());
        return firstChild instanceof PsiFunctorCall;
    }

    @Override
    public ItemPresentation getPresentation() {
        return new GoToSymbolProvider.BaseNavigationItem(this, getQualifiedName(), ORIcons.INCLUDE);
    }

    @NotNull
    @Override
    public String toString() {
        return "PsiInclude " + getQualifiedName();
    }
}

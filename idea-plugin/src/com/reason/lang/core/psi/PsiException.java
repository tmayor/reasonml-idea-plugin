package com.reason.lang.core.psi;

import org.jetbrains.annotations.Nullable;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.StubBasedPsiElement;
import com.reason.lang.core.stub.PsiExceptionStub;

public interface PsiException
        extends NavigatablePsiElement, PsiStructuredElement, PsiNameIdentifierOwner, PsiQualifiedElement, StubBasedPsiElement<PsiExceptionStub> {
    @Nullable String getAlias();
}

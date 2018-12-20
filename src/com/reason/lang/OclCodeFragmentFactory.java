package com.reason.lang;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.reason.lang.core.ORElementFactory;
import com.reason.lang.ocaml.OclLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OclCodeFragmentFactory {
    private final Project m_project;

    public static OclCodeFragmentFactory getInstance(Project project) {
        return ServiceManager.getService(project, OclCodeFragmentFactory.class);
    }

    public OclCodeFragmentFactory(Project project) {
        m_project = project;
    }

    public PsiFile createExpressionCodeFragment(@NotNull String text, @Nullable PsiElement context, @Nullable PsiType expectedType, boolean isPhysical) {
        return ORElementFactory.createFileFromText(m_project, OclLanguage.INSTANCE, text);
    }
}

package com.reason.lang.reason;

import java.util.*;
import org.jetbrains.annotations.NotNull;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ArrayListSet;
import com.reason.ide.files.FileBase;
import com.reason.lang.BaseQNameFinder;
import com.reason.lang.core.psi.PsiFunction;
import com.reason.lang.core.psi.PsiInclude;
import com.reason.lang.core.psi.PsiInnerModule;
import com.reason.lang.core.psi.PsiLet;
import com.reason.lang.core.psi.PsiLetBinding;
import com.reason.lang.core.psi.PsiLocalOpen;
import com.reason.lang.core.psi.PsiOpen;
import com.reason.lang.core.psi.PsiParameter;
import com.reason.lang.core.psi.PsiQualifiedElement;

public class RmlQNameFinder extends BaseQNameFinder {

    // Find the expression paths
    @NotNull
    public Set<String> extractPotentialPaths(@NotNull PsiElement element) {
        Set<String> qualifiedNames = new ArrayListSet<>();

        String path = extractPathName(element, RmlTypes.INSTANCE);
        String pathExtension = path.isEmpty() ? "" : "." + path;

        // Walk backward until top of the file is reached, trying to find local opens and opens/includes
        PsiElement item = element;
        while (item != null) {
            if (100 < qualifiedNames.size()) {
                break; // There must be a problem with the parser
            }

            if (item instanceof FileBase) {
                qualifiedNames.add(((FileBase) item).getModuleName() + pathExtension);
                break;
            } else if (item instanceof PsiInnerModule) {
                if (path.equals(((PsiInnerModule) item).getName())) {
                    qualifiedNames.add(((FileBase) element.getContainingFile()).getModuleName() + pathExtension);
                }
            } else if (item instanceof PsiLocalOpen) {
                String openName = extractPathName(item, RmlTypes.INSTANCE);
                // Add local open value to all previous elements
                qualifiedNames.addAll(extendPathWith(openName, qualifiedNames, pathExtension));
                qualifiedNames.add(openName + pathExtension);
            } else if (item instanceof PsiOpen || item instanceof PsiInclude) {
                String openName = ((PsiQualifiedElement) item).getQualifiedName();
                // Add open value to all previous elements
                qualifiedNames.addAll(extendPathWith(openName, qualifiedNames, pathExtension));
                qualifiedNames.add(openName + pathExtension);
            } else if (item instanceof PsiLetBinding) {
                // let a = { <caret> }
                PsiLet let = PsiTreeUtil.getParentOfType(item, PsiLet.class);
                if (let != null) {
                    String letQName = let.getQualifiedName();
                    qualifiedNames.addAll(extendPathWith(letQName, qualifiedNames, pathExtension));
                    qualifiedNames.add(letQName + pathExtension);
                }
            } else if (item instanceof PsiFunction) {
                PsiQualifiedElement parent = PsiTreeUtil.getParentOfType(item, PsiQualifiedElement.class);
                if (parent != null) {
                    String parentQName = parent.getQualifiedName();
                    // Register all parameters of function
                    for (PsiParameter parameter : ((PsiFunction) item).getParameters()) {
                        qualifiedNames.add(parentQName + "[" + parameter.getName() + "]");
                    }
                }
            }

            PsiElement prevItem = item.getPrevSibling();
            if (prevItem == null) {
                item = item.getParent();
            } else {
                item = prevItem;
            }
        }

        if (!path.isEmpty()) {
            qualifiedNames.add(path);
        }

        return qualifiedNames;
    }
}

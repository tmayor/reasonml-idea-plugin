package com.reason.compiler;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VirtualFile;
import com.reason.bs.Bucklescript;
import com.reason.dune.DuneCompiler;
import com.reason.esy.EsyCompiler;
import com.reason.ide.ORNotification;
import com.reason.ide.console.CliType;
import com.reason.ide.facet.DuneFacet;
import com.reason.ide.facet.EsyFacet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.intellij.notification.NotificationListener.URL_OPENING_LISTENER;
import static com.intellij.notification.NotificationType.ERROR;

class CompilerProvider {

    private static final Runnable SHOW_DUNE_SDK_MISSING = () -> Notifications.Bus.notify(
            new ORNotification("Dune", "<html>Can't find sdk.\n"
            + "When using a dune config file, you need to create an OCaml SDK and associate it to the project.\n"
            + "see <a href=\"https://github.com/reasonml-editor/reasonml-idea-plugin#ocaml\">github</a>.</html>",
            ERROR, URL_OPENING_LISTENER));

    private static final Compiler DUMMY_COMPILER = new Compiler() {

        @Override
        public Optional<VirtualFile> findContentRoot() {
            return Optional.empty();
        }

        @Override
        public void refresh(@NotNull VirtualFile bsconfigFile) {
            //nothing
        }

        @Override
        public void run(@NotNull VirtualFile file, @Nullable ProcessTerminated onProcessTerminated) {
            //nothing
        }

        @Override
        public void run(@NotNull VirtualFile file, @NotNull CliType cliType, @Nullable ProcessTerminated onProcessTerminated) {
            //nothing
        }

        @Nullable
        @Override
        public ConsoleView getConsoleView() {
            return null;
        }
    };

    public static Compiler getInstance(@NotNull Project project, @NotNull CliType cliType) {
        if (cliType instanceof CliType.Bs) {
            return ServiceManager.getService(project, Bucklescript.class);
        }
        if (cliType instanceof CliType.Dune) {
            return DuneCompiler.getInstance(project);
        }
        if (cliType instanceof CliType.Esy) {
            return EsyCompiler.getInstance(project);
        }
        return DUMMY_COMPILER;
    }

    public static Compiler getInstance(Module module) {
        if (module == null) {
            return DUMMY_COMPILER;
        }

        Project project = module.getProject();
        Optional<Facet<?>> facetOptional = getFacet(module);
        if (facetOptional.isPresent()) {
            Facet<?> facet = facetOptional.get();

            if (facet instanceof EsyFacet) {
                EsyFacet esyFacet = (EsyFacet) facet;
                if (!esyFacet.isSetupValid()) {
                    return DUMMY_COMPILER;
                }
                return EsyCompiler.getInstance(project);
            }

            if (facet instanceof DuneFacet) {
                DuneFacet duneFacet = (DuneFacet) facet;
                Sdk odk = duneFacet.getODK();
                if (odk == null) {
                    SHOW_DUNE_SDK_MISSING.run();
                    return DUMMY_COMPILER;
                }
                return DuneCompiler.getInstance(project);
            }
        }
        //return DUMMY_COMPILER;
        return ServiceManager.getService(project, Bucklescript.class);
    }

    private static Optional<Facet<?>> getFacet(Module module) {
        FacetManager facetManager = FacetManager.getInstance(module);
        // let esy take precedence over dune
        Optional<Facet<?>> esyFacet = Optional.ofNullable(facetManager.getFacetByType(EsyFacet.ID));
        if (esyFacet.isPresent()) {
            return esyFacet;
        }
        Optional<Facet<?>> duneFacet = Optional.ofNullable(facetManager.getFacetByType(DuneFacet.ID));
        if (duneFacet.isPresent()) {
            return duneFacet;
        }

        // @TODO create BuckleScript facet...
        return Optional.empty();
    }
}

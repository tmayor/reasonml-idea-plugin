package com.reason.ide.debug;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil;
import com.intellij.openapi.externalSystem.util.PaintAwarePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class OclSettingsEditor extends SettingsEditor<OCamlApplicationConfiguration> {
    @Override
    protected void resetEditorFrom(@NotNull OCamlApplicationConfiguration s) {

    }

    @Override
    protected void applyEditorTo(@NotNull OCamlApplicationConfiguration s) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        PaintAwarePanel result = new PaintAwarePanel(new GridBagLayout());
//        myControl.fillUi(result, 0);
        result.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 200), new Dimension(0, 0)),
                ExternalSystemUiUtil.getFillLineConstraints(0));
        ExternalSystemUiUtil.fillBottom(result);
        return result;
    }
}

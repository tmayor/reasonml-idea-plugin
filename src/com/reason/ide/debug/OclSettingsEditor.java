package com.reason.ide.debug;

import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil;
import com.intellij.openapi.externalSystem.util.PaintAwarePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.reason.ide.debug.conf.OclRunConfiguration;
import com.reason.ide.debug.conf.OclRunConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class OclSettingsEditor extends SettingsEditor<OclRunConfiguration> {
    @Override
    protected void resetEditorFrom(@NotNull OclRunConfiguration s) {

    }

    @Override
    protected void applyEditorTo(@NotNull OclRunConfiguration s) throws ConfigurationException {

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

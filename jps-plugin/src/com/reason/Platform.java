package com.reason;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Platform {

    public static final String LOCAL_BS_PLATFORM = "/node_modules/bs-platform";
    public static final String LOCAL_NODE_MODULES_BIN = "/node_modules/.bin";
    public static final Charset UTF8 = StandardCharsets.UTF_8;

    @NotNull
    public static String getOsPrefix() {
        if (SystemInfo.isWindows) {
            return "w";
        }

        if (SystemInfo.isLinux) {
            return "l";
        }

        if (SystemInfo.isMac) {
            return "o";
        }

        return "";
    }

    public static boolean isWindows() {
        return SystemInfo.isWindows;
    }

    @Nullable
    public static File getPluginLocation() {
        IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId("reasonml"));
        return plugin == null ? null : plugin.getPath();
    }

    private Platform() {}
}

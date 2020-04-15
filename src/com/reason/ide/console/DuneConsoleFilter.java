package com.reason.ide.console;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.OpenFileHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

class DuneConsoleFilter implements Filter {
    private final static Pattern LINE_PATTERN = Pattern.compile("^\\s*(.+.(?:rei|re|mli|ml))\\s([0-9]+):([0-9]+)[-0-9:\\s]*$");

    @NotNull
    private final Project m_project;

    DuneConsoleFilter(@NotNull Project project) {
        m_project = project;
    }

    @Nullable
    @Override
    public Result applyFilter(@NotNull String line, int entireLength) {
        int startPoint = entireLength - line.length();
        Matcher matcher = LINE_PATTERN.matcher(line);
        if (matcher.find()) {
            String filePath = matcher.group(1);
            VirtualFile virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(filePath);
            if (virtualFile != null) {
                return new Result(startPoint + matcher.start(1), entireLength,
                        new OpenFileHyperlinkInfo(m_project, virtualFile, parseInt(matcher.group(2)) - 1, parseInt(matcher.group(3)) - 1));
            }
        }
        return null;
    }
}

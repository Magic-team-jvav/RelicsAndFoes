package org.mesdag.accessor;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenerateAccessorStubsTask extends DefaultTask {

    @InputFiles
    public abstract ConfigurableFileCollection getResourceDirs();

    @InputFile
    public abstract RegularFileProperty getClasspathFile();

    @InputFiles
    public abstract ConfigurableFileCollection getArtifactJars();

    @OutputDirectory
    public abstract DirectoryProperty getStubsDir();

    @TaskAction
    public void generate() throws IOException {
        List<File> resourceDirs = new ArrayList<>();
        getResourceDirs().forEach(resourceDirs::add);

        List<File> classpath = new ArrayList<>();

        File classpathFile = getClasspathFile().get().getAsFile();
        if (classpathFile.exists()) {
            for (String line : Files.readAllLines(classpathFile.toPath())) {
                line = line.trim();
                if (!line.isEmpty()) {
                    File f = new File(line);
                    if (f.exists()) {
                        classpath.add(f);
                    }
                }
            }
        }

        getArtifactJars().forEach(classpath::add);

        File stubsDir = getStubsDir().get().getAsFile();
        stubsDir.mkdirs();

        Map<String, List<String>> config = AccessorStubGenerator.collectConfigs(resourceDirs, classpath);

        if (config.isEmpty()) {
            getLogger().lifecycle("[AccessorStub] No accessor_interfaces.json found on classpath");
            return;
        }

        getLogger().lifecycle("[AccessorStub] Injecting {} target(s)", config.size());
        AccessorStubGenerator.generate(config, classpath, stubsDir);
    }
}

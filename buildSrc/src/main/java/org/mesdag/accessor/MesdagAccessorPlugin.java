package org.mesdag.accessor;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.File;

public class MesdagAccessorPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPlugins().withType(org.gradle.api.plugins.JavaPlugin.class, javaPlugin -> {
            var sourceSets = project.getExtensions()
                    .getByType(JavaPluginExtension.class).getSourceSets();
            SourceSet main = sourceSets.getByName("main");

            File stubsDir = project.getLayout().getBuildDirectory()
                    .dir("accessorStubs").get().getAsFile();

            File classpathFile = project.getLayout().getBuildDirectory()
                    .file("moddev/clientLegacyClasspath.txt").get().getAsFile();

            File artifactsDir = project.getLayout().getBuildDirectory()
                    .dir("moddev/artifacts").get().getAsFile();

            var stubTask = project.getTasks().register("generateAccessorStubs",
                    GenerateAccessorStubsTask.class, task -> {
                        task.setGroup("mesdag");
                        task.setDescription(
                                "Read accessor_interfaces.json and generate ASM stubs");

                        task.getClasspathFile().set(classpathFile);
                        task.getStubsDir().set(stubsDir);

                        for (File dir : main.getResources().getSrcDirs()) {
                            task.getResourceDirs().from(dir);
                        }

                        task.getArtifactJars().from(
                                project.fileTree(artifactsDir, ft -> {
                                    ft.include("*-merged.jar");
                                }));

                        task.dependsOn("writeClientLegacyClasspath",
                                "createMinecraftArtifacts");

                        for (Project dep : project.getAllprojects()) {
                            if (dep == project) continue;
                            dep.getPlugins().withType(
                                    org.gradle.api.plugins.JavaPlugin.class,
                                    __ -> {
                                        var depMain = dep.getExtensions()
                                                .getByType(JavaPluginExtension.class)
                                                .getSourceSets()
                                                .getByName("main");
                                        for (File dir : depMain.getResources()
                                                .getSrcDirs()) {
                                            task.getResourceDirs().from(dir);
                                        }
                                    });
                        }
                    });

            project.getTasks().named("compileJava", JavaCompile.class, compileJava -> {
                compileJava.dependsOn(stubTask);
                compileJava.setClasspath(
                        project.files(stubsDir)
                                .plus(compileJava.getClasspath()));
            });

            project.getDependencies().add("compileOnly",
                    project.files(stubsDir));
        });
    }
}

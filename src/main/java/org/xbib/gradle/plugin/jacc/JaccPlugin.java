package org.xbib.gradle.plugin.jacc;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;

import javax.inject.Inject;
import java.io.File;

public class JaccPlugin implements Plugin<Project> {

    private final FileResolver fileResolver;

    @Inject
    public JaccPlugin(final FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    @Override
    public void apply(final Project project) {
        project.getPlugins().apply(JavaPlugin.class);
        configureConfigurations(project);
        configureSourceSets(project);
    }

    private void configureConfigurations(Project project) {
        final Configuration jaccConfiguration =
                project.getConfigurations().create("jacc").setVisible(false);
        project.getConfigurations().getByName(JavaPlugin.COMPILE_CONFIGURATION_NAME).extendsFrom(jaccConfiguration);
    }

    private void configureSourceSets(final Project project) {
        project.getConvention()
                .getPlugin(JavaPluginConvention.class).getSourceSets()
                .all(sourceSet -> {
                    JaccVirtualSourceDirectoryImpl jaccSourceSet =
                            new JaccVirtualSourceDirectoryImpl(((DefaultSourceSet) sourceSet).getDisplayName(), fileResolver);
                    new DslObject(sourceSet).getConvention().getPlugins().put("jacc", jaccSourceSet);
                    String srcDir = String.format("src/%s/jacc", sourceSet.getName());
                    jaccSourceSet.getJacc().srcDir(srcDir);
                    sourceSet.getAllSource().source(jaccSourceSet.getJacc());
                    String taskName = sourceSet.getTaskName("generate", "JaccSource");
                    JaccTask jaccTask = project.getTasks().create(taskName, JaccTask.class);
                    jaccTask.setDescription(String.format("Processes the %s Jacc files.", sourceSet.getName()));
                    jaccTask.setSource(jaccSourceSet.getJacc());
                    jaccTask.getConventionMapping().map("jaccClasspath",
                            () -> project.getConfigurations().getByName("jacc").copy().setTransitive(true));
                    String outputDirectoryName =
                            String.format("%s/generated-src/jacc/%s", project.getBuildDir(), sourceSet.getName());
                    File outputDirectory = new File(outputDirectoryName);
                    jaccTask.setOutputDirectory(outputDirectory);
                    sourceSet.getJava().srcDir(outputDirectory);
                    project.getTasks().getByName(sourceSet.getCompileJavaTaskName()).dependsOn(taskName);
                });
    }
}

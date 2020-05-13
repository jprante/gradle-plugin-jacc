package org.xbib.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskProvider

class JaccPlugin implements Plugin<Project> {

    private static final Logger logger = Logging.getLogger(JaccPlugin)

    @Override
    void apply(Project project) {
        project.with {
            apply plugin: 'java-library'
            createExtension(project)
            addSourceSetExtensions(project)
        }
        project.afterEvaluate {
            addTasks(project)
        }
    }

    private static void createExtension(Project project) {
        project.extensions.create ('jacc', JaccExtension)
    }

    private static void addSourceSetExtensions(Project project) {
        project.sourceSets.all { SourceSet sourceSet ->
            createSourceSetExtension(project, sourceSet)
            createConfiguration(project, sourceSet)
        }
    }

    private static void createSourceSetExtension(Project project, SourceSet sourceSet) {
        String name = sourceSet.name
        SourceDirectorySet sourceDirectorySet = project.objects.sourceDirectorySet(name, "${name} Jacc source")
        sourceSet.extensions.add('jacc', sourceDirectorySet)
        sourceDirectorySet.srcDir("src/${name}/jacc")
        sourceDirectorySet.include("**/*.jacc")
    }


    private static void createConfiguration(Project project, SourceSet sourceSet) {
        String configName = sourceSet.name + 'Jacc'
        if (project.configurations.findByName(configName) == null) {
            logger.info "create configuration ${configName}"
            project.configurations.create(configName) {
                visible = false
                transitive = true
                extendsFrom = []
            }
        }
    }

    private static void addTasks(Project project) {
        project.sourceSets.all { SourceSet sourceSet ->
            addTaskForSourceSet(project, sourceSet)
        }
    }

    private static void addTaskForSourceSet(Project project, SourceSet sourceSet) {
        String taskName = sourceSet.getTaskName('generate', 'jacc')
        SourceDirectorySet sourceDirectorySet = sourceSet.extensions.getByName('jacc') as SourceDirectorySet
        File targetFile = sourceSet.java && sourceSet.java.srcDirs ? sourceSet.java.srcDirs.last() :
                project.file("${project.buildDir}/generated/sources/${sourceSet.name}")
        if (sourceDirectorySet.asList()) {
            TaskProvider<JaccTask> taskProvider = project.tasks.register(taskName, JaccTask) {
                group = 'jacc'
                description = 'Generates code from Jacc files in ' + sourceSet.name
                source = sourceDirectorySet.asList()
                target = targetFile
            }
            logger.info "created ${taskName} for sources ${sourceDirectorySet.asList()} and target ${targetFile}"
            project.tasks.findByName(sourceSet.compileJavaTaskName).dependsOn taskProvider
            if (sourceSet.java && sourceSet.java.srcDirs) {
                sourceSet.java.srcDirs += targetFile
            }
        }
    }
}

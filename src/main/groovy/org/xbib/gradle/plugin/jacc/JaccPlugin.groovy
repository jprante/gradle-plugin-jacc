package org.xbib.gradle.plugin.jacc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.xbib.gradle.task.JaccTask

class JaccPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.with {
            apply plugin: 'java'
            tasks.create(name: 'jacc', type: JaccTask)
            tasks.compileJava.dependsOn tasks.jacc
            sourceSets.main.java.srcDirs += tasks.jacc.generateDir
        }
    }
}

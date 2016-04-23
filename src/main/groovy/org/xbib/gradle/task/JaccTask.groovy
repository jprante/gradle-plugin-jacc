package org.xbib.gradle.task

import jacc.CommandLine
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class JaccTask extends DefaultTask {

    @InputDirectory
    @Optional
    File source = project.file('src/main/jacc')

    @OutputDirectory
    @Optional
    File generateDir = project.file("$project.buildDir/generated-src/jacc")

    @TaskAction
    void generateAndTransformJacc() throws Exception {
        project.delete(generateDir)
        project.mkdir(generateDir)
        generateJacc()
    }

    private void generateJacc() {
        project.fileTree(dir:source, include:'**/*.jacc').visit { FileVisitDetails file ->
            if (file.isDirectory()) {
                return
            }
            CommandLine.main([file.file.absolutePath,
                              '-d',
                              project.file("$generateDir/${file.relativePath.parent}").absolutePath + '/'] as String[])
        }
    }
}

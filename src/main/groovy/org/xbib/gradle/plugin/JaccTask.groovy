package org.xbib.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.xbib.jacc.Jacc

class JaccTask extends DefaultTask {

    @InputFiles
    Iterable<File> source

    @OutputDirectory
    File target

    @TaskAction
    void generateAndTransformJacc() throws Exception {
        JaccExtension ext = project.extensions.findByType(JaccExtension)
        source.each { file ->
            String pkg = getPackageName(file)
            File fullTarget = new File(target, pkg.replace('.','/'))
            project.mkdir(fullTarget)
            Jacc.main([file.absolutePath, '-d', fullTarget] as String[])
        }
    }

    static String getPackageName(File file) {
        String string = file.readLines().find { line ->
            line.startsWith('package')
        }
        return string == null ? '' : string.substring(8, string.length() - 1)
    }
}

package org.xbib.gradle.plugin.jacc;

import jacc.CommandLine;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JaccTask extends SourceTask {

    private FileCollection fileCollection;

    private File outputDirectory;

    @TaskAction
    public void generate() throws Exception {
        for (final File sourceFile : getSource().getFiles()) {
            File outDir = findOutdir(sourceFile.getAbsolutePath(), getOutputDirectory().getAbsolutePath());
            outDir.mkdirs();
            CommandLine.main(new String[]{
                    sourceFile.getAbsolutePath(), "-d", outDir.getAbsolutePath() + "/"
            });
        }
    }

    @InputFiles
    public FileCollection getJaccClasspath() {
        return fileCollection;
    }

    public void setJaccClasspath(final FileCollection fileCollection) {
        this.fileCollection = fileCollection;
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(final File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Find output directory and add package name, if given
     */
    private File findOutdir(String infile, String outdir) throws IOException {
        File destDir;
        if (outdir != null) {
            String packageName = findPackage(infile);
            if (packageName == null) {
                destDir = new File(outdir);
            } else {
                destDir = new File(outdir, packageName.replace('.', File.separatorChar));
            }
        } else {
            destDir = new File(new File(infile).getParent());
        }
        return destDir;
    }

    /**
     * Peek into .jacc file to get package name
     *
     * @throws java.io.IOException if there is a problem reading the .jacc file
     */
    private String findPackage(String infile) throws IOException {
        String packageName = null;
        BufferedReader reader = new BufferedReader(new FileReader(infile));
        String line;
        while (packageName == null) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            int index = line.indexOf("package");
            if (index >= 0) {
                packageName = line.substring(index + 7).trim();
            }
        }
        return packageName;
    }

}

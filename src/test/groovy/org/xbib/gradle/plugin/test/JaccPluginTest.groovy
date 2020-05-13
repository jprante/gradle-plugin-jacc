package org.xbib.gradle.plugin.test

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

import static org.junit.jupiter.api.Assertions.*

class JaccPluginTest {


    private File projectDir

    private File settingsFile

    private File buildFile

    @BeforeEach
    void setup(@TempDir File testProjectDir) throws IOException {
        this.projectDir = testProjectDir
        this.settingsFile = new File(testProjectDir, "settings.gradle")
        this.buildFile = new File(testProjectDir, "build.gradle")
    }

    @Test
    void testJacc() {
        String settingsFileContent = '''
rootProject.name = 'jacc-test'
'''
        settingsFile.write(settingsFileContent)
        String buildFileContent = '''
plugins {
    id 'org.xbib.gradle.plugin.jacc'
}

sourceSets {
  test {
     jacc {
       srcDir "${System.getProperty('user.dir')}/src/test/jacc"
     }
     java {
       srcDir "${System.getProperty('user.dir')}/build/my-generated-sources/jacc"
     }
  }
}

'''
        buildFile.write(buildFileContent)
        BuildResult result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withArguments(":build", "--info")
                .withPluginClasspath()
                .forwardOutput()
                .build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":build").getOutcome())

        File file = new File("${System.getProperty('user.dir')}/build/my-generated-sources/jacc")
        if (file.exists()) {
            List<File> list = Arrays.asList(file.listFiles())
            assertEquals(2, list.size())
        }
    }
}

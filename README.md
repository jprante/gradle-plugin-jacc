# gradle-plugin-jacc

A Gradle plugin for [Jacc](http://web.cecs.pdx.edu/~mpj/jacc/)

# Versions

1.0.3 - for Gradle < 2.12

1.0.4 - for Gradle >= 2.12

## Usage

    apply plugin: 'java'
    apply plugin: 'jacc'

    buildscript {
        repositories {
            mavenCentral()
            maven {
                url "http://xbib.org/repository"
            }
        }
        dependencies {
            classpath 'org.xbib.gradle.plugins:gradle-plugin-jacc:1.0.4'
        }
    }

    dependencies {
        jacc 'org.xbib:jacc:2.0.0-SNAPSHOT'
    }

Gradle will look for your Jacc files in `src/main/jacc/*.jacc`.

# License

Copyright (C) 2015 Jörg Prante

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


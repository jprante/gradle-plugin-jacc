# gradle-plugin-jacc

A Gradle plugin for [Jacc](http://web.cecs.pdx.edu/~mpj/jacc/)

## Compatibility

| Date | Plugin | Gradle | Jacc |
|----- | ------ | ------ | ----- |
| 2020-05-13 | 1.4.0 | 6.4 | 2.2.0 |

## Usage

        plugins {
           id 'org.xbib.gradle.plugin.jacc'
        }
        
        apply plugin: 'org.xbib.gradle.plugin.jacc'

Gradle will look for your jacc files in the source sets you specified.
By default, it looks with the pattern `**/*.jacc` under `src/main/jacc`
and `src/test/jacc`.

You can set up the source sets like this:

    sourceSets {
      main {
         jacc {
           srcDir "src/main/jacc"
         }
         java {
           srcDir "build/my-generated-sources/jacc"
         }
      }
    }
    
The lastJava `srcDir` definition will be used as the base for the Jacc target path.
If not given, the Jacc target path for generated Java source follows the pattern:

`${project.buildDir}/generated/sources/jacc`

The Jacc target path will be added automaticlly to the java compile task source directory 
of the source set.

# License

Copyright (C) 2015-2020 Jörg Prante

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

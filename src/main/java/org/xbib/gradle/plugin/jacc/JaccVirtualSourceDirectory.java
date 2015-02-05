package org.xbib.gradle.plugin.jacc;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;

public interface JaccVirtualSourceDirectory {

    SourceDirectorySet getJacc();

    JaccVirtualSourceDirectory jacc(Closure closure);
}

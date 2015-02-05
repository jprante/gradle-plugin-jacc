package org.xbib.gradle.plugin.jacc;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.util.ConfigureUtil;

public class JaccVirtualSourceDirectoryImpl implements JaccVirtualSourceDirectory {

    private final SourceDirectorySet jflex;

    public JaccVirtualSourceDirectoryImpl(final String parentDisplayName, FileResolver fileResolver) {
        final String displayName = String.format("%s Jacc source", parentDisplayName);
        this.jflex = new DefaultSourceDirectorySet(displayName, fileResolver);
        this.jflex.getFilter().include("**/*.jacc");
    }

    @Override
    public SourceDirectorySet getJacc() {
        return jflex;
    }

    @Override
    public JaccVirtualSourceDirectory jacc(Closure closure) {
        ConfigureUtil.configure(closure, getJacc());
        return this;
    }
}

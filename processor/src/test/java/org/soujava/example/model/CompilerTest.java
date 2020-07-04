package org.soujava.example.model;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import java.io.IOException;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

public class CompilerTest {


    @Test
    public void shouldCompile() throws IOException {
        Compilation compilation = javac()
                .withClasspathFrom(Entity.class.getClassLoader())
                .withOptions()
                .withProcessors(new EntityProcessor())
                .compile(
                        JavaFileObjects.forResource("Person.java"));
        assertThat(compilation).succeeded();
    }

    @Test
    public void shouldReturnConstructorIssue() throws IOException {
        Compilation compilation = javac()
                .withClasspathFrom(Entity.class.getClassLoader())
                .withOptions()
                .withProcessors(new EntityProcessor())
                .compile(
                        JavaFileObjects.forResource("Person.java"));
        assertThat(compilation).succeeded();
    }
}

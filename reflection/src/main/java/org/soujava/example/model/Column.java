package org.soujava.example.model;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
public @interface Column {
    String value() default "";
}

package org.soujava.medatadata.api;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
public @interface Column {
    String value() default "";
}

package org.soujava.medatadata.api;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
public @interface Entity {
    String value() default "";
}

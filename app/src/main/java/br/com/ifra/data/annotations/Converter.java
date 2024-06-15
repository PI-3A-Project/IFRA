package br.com.ifra.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Converter {
    String dataPattern() default "yyyy/MM/dd";

    String dataPatternDefault() default "yyyy/MM/dd";

    boolean value() default true;
}

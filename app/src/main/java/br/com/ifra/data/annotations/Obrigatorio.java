package br.com.ifra.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Obrigatorio {
}

package club.orchid.anno.mapping

import groovy.transform.AnnotationCollector

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/1/16 6:58 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Persistent
@Inherited
@interface Primitive {
    String value() default ""
}
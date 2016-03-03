package club.orchid.anno.mapping

import groovy.transform.AnnotationCollector

import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/1/16 7:00 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Persistent
@Inherited
@interface Id {
    String value() default ""
}

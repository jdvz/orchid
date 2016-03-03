package club.orchid.anno.mapping

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 5:40 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Persistent
@Inherited
@interface ManyToOne {
    String value()
}
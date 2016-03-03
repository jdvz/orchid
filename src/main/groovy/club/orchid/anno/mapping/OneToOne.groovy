package club.orchid.anno.mapping

import java.lang.annotation.*

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 5:40 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Persistent
@Inherited
@interface OneToOne {
    String value()
}
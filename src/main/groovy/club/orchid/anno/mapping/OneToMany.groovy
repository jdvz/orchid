package club.orchid.anno.mapping

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Persistent
@Inherited
@interface OneToMany {
    String value()
}
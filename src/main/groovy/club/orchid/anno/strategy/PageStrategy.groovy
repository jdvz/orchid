package club.orchid.anno.strategy

import club.orchid.domain.cms.Page

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 10:31 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@interface PageStrategy {
    Class<? extends Page> value()
}
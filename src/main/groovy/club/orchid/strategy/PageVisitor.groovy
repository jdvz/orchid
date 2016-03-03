package club.orchid.strategy

import club.orchid.domain.cms.Page

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 1:24 AM
 */
interface PageVisitor <T extends Page> {
    boolean accept(T page)
    boolean accept(String className)
}
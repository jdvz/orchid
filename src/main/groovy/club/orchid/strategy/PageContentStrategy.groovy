package club.orchid.strategy

import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.Page

import java.sql.ResultSet

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 1:22 AM
 */
abstract class PageContentStrategy<T extends Page> implements PageVisitor<T> {
    public abstract void extractContent(ResultSet rs, int rowNum, T page)
    public abstract T createPage()
    public abstract Class<T> getPageClass()

    @Override
    boolean accept(T page) {
        return page && getPageClass().simpleName.equals(page.getDiscriminator())
    }

    @Override
    boolean accept(String className) {
        return className && getPageClass().simpleName.equals(className)
    }
}

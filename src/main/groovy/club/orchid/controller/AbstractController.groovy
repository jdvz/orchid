package club.orchid.controller

import club.orchid.domain.cms.Page
import club.orchid.service.IPageService
import club.orchid.service.IUserService
import club.orchid.service.PageService
import club.orchid.strategy.CmsPageContentStrategy
import club.orchid.strategy.ContentPageContentStrategy
import club.orchid.strategy.PageContentStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:09 AM
 */
class AbstractController {
    @Autowired
    protected IUserService userService
}

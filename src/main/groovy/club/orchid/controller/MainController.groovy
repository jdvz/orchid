package club.orchid.controller

import club.orchid.domain.cms.Image
import club.orchid.service.IPageService
import club.orchid.service.IUserService
import club.orchid.service.UserService
import groovy.util.logging.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import java.security.Principal

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 1:15 PM
 */
@Controller
@RequestMapping('/')
@Log
class MainController extends AbstractController {
    @Autowired
    private IPageService pageService

    @RequestMapping(value = ['/','/index.html'], method = RequestMethod.GET)
    public String main(final Model model) {
        log.info('passed')
        model.addAttribute('title', 'start')
        model.addAttribute('pages', pageService.pages())
        return 'index'
    }

    @RequestMapping(value = ['/test.html'], method = RequestMethod.GET)
    public String test(final Model model) {
        model.addAttribute('title', 'test title')
        model.addAttribute('templateName', "layout/main")
        return 'test'
    }

    @RequestMapping(value = ['/test/{templateName}.html'], method = RequestMethod.GET)
    public String variant(@PathVariable final String templateName, final Model model) {
        model.addAttribute('DECORATOR_TITLE', 'Variant page')
        model.addAttribute('CONTENT_TITLE', 'Main content')
        model.addAttribute('templateName', "layout/$templateName")
        return 'test'
    }
}

package club.orchid.controller

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.Page
import club.orchid.service.IPageService
import club.orchid.service.MainApplicationContext
import club.orchid.util.AuthenticationUtils
import club.orchid.web.forms.PageCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import java.security.Principal

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:30 AM
 */
@Controller
@RequestMapping(value = '/page')
class PageController extends AbstractController {
    @Autowired
    private IPageService pageService
    @Autowired
    protected MainApplicationContext context

    protected List<Page> pages(long mainPageId) {
        if (mainPageId) {
            pageService.subpages(mainPageId)
        } else {
            pageService.pages()
        }
    }

    @RequestMapping(value = '/{prettyUrl}.html', method = RequestMethod.GET)
    public String page(@PathVariable final String prettyUrl, final Model model, final Principal principal) {
        Page page = pageService.page(prettyUrl).orElse(AuthenticationUtils.page404)
        model.addAttribute('templateName', "layout/${page.template}")
        model.addAttribute('page', page)
        model.addAttribute('pages', pageService.contentpages())
        model.addAttribute('current', page.currentCatalogId)
        model.addAttribute('user', userService.loadUserByUsername(principal?.name))
        return "${page.pageName}"
    }

    @RequestMapping(value = '/{prettyUrl}/{pageNum}.html', method = RequestMethod.GET)
    public String page(@PathVariable final String prettyUrl, @PathVariable final int pageNum, final Model model, final Principal principal) {
        CmsPage page = pageService.<CmsPage> page(prettyUrl).orElse(AuthenticationUtils.page404)
        model.addAttribute('templateName', "layout/${page.template}")
        model.addAttribute('page', page)
        model.addAttribute('pages', pageService.contentpages())

        model.addAttribute('pageNum', pageNum)
        if (pageNum > 0) {
            model.addAttribute('previousPage', "$page.prettyUrl/${pageNum - 1}")
        }

        model.addAttribute('user', userService.loadUserByUsername(principal?.name))
        return "${page.pageName}"
    }

    @Secured('ADMIN')
    @RequestMapping(value = '/{prettyUrl}/edit.html', method = RequestMethod.GET)
    public String edit(@PathVariable final String prettyUrl, final Model model, final Principal principal, final RedirectAttributes redirectAttributes) {
        Optional<CmsPage> optional = pageService.<CmsPage> page(prettyUrl)
        if (optional.isPresent()) {
            CmsPage page = optional.get()
            model.addAttribute('pageCommand', new PageCommand(
                    prettyUrl: prettyUrl,
                    content: page.content,
                    type: page.discriminator,
                    template: page.template,
                    contentPageId: page.contentPage?.id,
                    types: ['CmsPage'],
                    templates: ['edit', 'empty', 'home', 'main', 'page']
            ))
            return "edit"
        } else {
            redirectAttributes.addFlashAttribute('message', 'Can\'t edit page')
            return "redirect:/pages/${prettyUrl}.html"
        }
    }

    @Secured('ADMIN')
    @RequestMapping(value = '/{prettyUrl}/edit.html', method = RequestMethod.POST)
    public String edit(@PathVariable final String prettyUrl,
                       final PageCommand pageCommand,
                       final Model model, final Principal principal, final RedirectAttributes redirectAttributes) {
        CmsPage page = pageService.<CmsPage> page(prettyUrl).orElse(new CmsPage(discriminator: 'CmsPage'))
        pageService.save(page, pageCommand)
        return "redirect:/pages/${prettyUrl}.html"
    }
}

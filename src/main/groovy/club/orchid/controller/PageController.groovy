package club.orchid.controller

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.CmsPageContent
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.Page
import club.orchid.service.IPageService
import club.orchid.service.MainApplicationContext
import club.orchid.util.AuthenticationUtils
import club.orchid.web.forms.PageCommand
import com.sun.beans.editors.StringEditor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import javax.validation.Valid
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
        Optional<Page> optional = pageService.<Page> page(prettyUrl)
        if (optional.isPresent()) {
            Page page = optional.get()
            model.addAttribute('pageCommand', new PageCommand(
                    pageId: page.id,
                    prettyUrl: prettyUrl,
                    content: page.content,
                    name: page.name,
                    type: page.discriminator,
                    template: page.template,
                    contentPageId: page.contentPage?.id ?: 0,
                    types: pageService.allowedTypes(),
                    templates: pageService.allowedTemplates(),
                    contentPages: pageService.contentpages()
            ))
            return "pages/edit"
        } else {
            redirectAttributes.addFlashAttribute('message', 'Can\'t edit page')
            return "redirect:/pages/${prettyUrl}.html"
        }
    }

    @Secured('ADMIN')
    @RequestMapping(value = '/{prettyUrl}/create.html', method = RequestMethod.GET)
    public String create(@PathVariable final String prettyUrl, final Model model, final Principal principal, final RedirectAttributes redirectAttributes) {
        model.addAttribute('pageCommand', new PageCommand(
                pageId: 0,
                prettyUrl: prettyUrl,
                content: '',
                name: '',
                type: 'CmsPageContent',
                template: 'page',
                contentPageId: 0,
                types: pageService.allowedTypes(),
                templates: pageService.allowedTemplates(),
                contentPages: pageService.contentpages()
        ))
        return "pages/edit"
    }

    @Secured('ADMIN')
    @RequestMapping(value = '/{prettyUrl}/edit.html', method = RequestMethod.POST)
    public String edit(@PathVariable final String prettyUrl,
                       @Valid final PageCommand pageCommand,
                       final Model model, final Principal principal, final RedirectAttributes redirectAttributes) {
        try {
            pageService.save(pageCommand)
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute('message', "Can\'t save ${prettyUrl}")
        }
        return "redirect:/page/${pageCommand.prettyUrl}.html"
    }
}

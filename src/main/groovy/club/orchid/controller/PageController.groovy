package club.orchid.controller

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.Page
import club.orchid.service.IPageService
import club.orchid.service.impl.MainApplicationContext
import club.orchid.util.AuthenticationUtils
import club.orchid.web.forms.PageCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import javax.validation.Valid

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

    @RequestMapping(value = '/{prettyUrl}.html', method = RequestMethod.GET)
    public String page(@PathVariable final String prettyUrl, final Model model) {
        CmsPage page = pageService.page(prettyUrl).orElse(AuthenticationUtils.page404)
        model.addAttribute('templateName', "layout/${page.template}")
        model.addAttribute('page', page)
        model.addAttribute('pages', pageService.pages())
        model.addAttribute('current', page.mainPage?.id)
        return "${page.pageName}"
    }

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/{prettyUrl}/edit.html', method = RequestMethod.GET)
    public String edit(@PathVariable final String prettyUrl, final Model model, final RedirectAttributes redirectAttributes) {
        Optional<CmsPage> optional = pageService.page(prettyUrl)
        if (optional.isPresent()) {
            Page page = optional.get()
            model.addAttribute('pageCommand', new PageCommand(
                    pageId: page.id,
                    prettyUrl: prettyUrl,
                    content: page.content,
                    name: page.name,
                    type: page.discriminator,
                    template: page.template,
                    mainPageId: page.mainPage?.id ?: 0,
                    types: pageService.allowedTypes(),
                    templates: pageService.allowedTemplates(),
                    contentPages: pageService.allowedPages()
            ))
            return "pages/edit"
        } else {
            redirectAttributes.addFlashAttribute('message', 'Can\'t edit page')
            return "redirect:/pages/${prettyUrl}.html"
        }
    }

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/{prettyUrl}/create.html', method = RequestMethod.GET)
    public String create(@PathVariable final String prettyUrl, @RequestParam(required = false, defaultValue = '0') final long pageId,
                         final Model model) {
        model.addAttribute('pageCommand', new PageCommand(
                pageId: 0,
                prettyUrl: prettyUrl,
                content: '',
                name: '',
                type: 'CmsPageContent',
                template: 'page',
                mainPageId: pageId,
                types: pageService.allowedTypes(),
                templates: pageService.allowedTemplates(),
                contentPages: pageService.allowedPages()
        ))
        return "pages/edit"
    }

    @Secured('ROLE_ADMIN')
    @RequestMapping(value = '/{prettyUrl}/edit.html', method = RequestMethod.POST)
    public String edit(@PathVariable final String prettyUrl,
                       @Valid final PageCommand pageCommand,
                       final Model model, final RedirectAttributes redirectAttributes) {
        try {
            pageService.save(pageCommand)
            redirectAttributes.addFlashAttribute('message', 'Successful')
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute('message', "Can\'t save ${prettyUrl}")
        }
        return "redirect:/page/${pageCommand.prettyUrl}.html"
    }
}

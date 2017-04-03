package club.orchid.service.impl

import club.orchid.Constants
import club.orchid.dao.cms.PageRepository
import club.orchid.domain.cms.CmsPage
import club.orchid.service.IPageService
import club.orchid.service.IResourceService
import club.orchid.strategy.PageContentStrategy
import club.orchid.web.forms.PageCommand
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:32 AM
 */
@Service
class PageService implements IPageService {
    private static final Logger log = Logger.getLogger(PageService.class.name)
    @Autowired
    private PageRepository pageRepository
    @Autowired
    private IResourceService resourceService
    @Autowired
    private MainApplicationContext context
    @Autowired
    @Qualifier('cmsPageContentStrategy')
    private PageContentStrategy<CmsPage> strategy

    @Override
    List<CmsPage> pages() {
        return pageRepository.pages()
    }

    @Override
    List<CmsPage> pages(long mainPageId) {
        return pageRepository.pages(mainPageId)
    }

    @Override
    Optional<CmsPage> page(String prettyUrl) {
        return pageRepository.page(prettyUrl)
    }

    @Override
    def Optional<CmsPage> page(long pageId) {
        return pageRepository.page(pageId)
    }

    @Override
    String pageContent(long pageId) {
        String contentPath = pageRepository.pageContentPath(pageId)
        return contentPath ? resourceService.getCmsFullPageContent(contentPath) : ''
    }

    @Override
    @Transactional
    CmsPage save(PageCommand pageCommand) {
        boolean createNew = pageCommand.pageId == 0
        if (createNew) {
            final CmsPage newpage = pageRepository.create(pageCommand)
            resourceService.create(pageCommand)
            return newpage
        } else {
            CmsPage page = pageRepository.page(pageCommand.pageId).orElseThrow({
                return new IllegalArgumentException("Page not found")
            })
            pageRepository.update(page, pageCommand)
            if (page.prettyUrl != pageCommand.prettyUrl || StringUtils.isEmpty(pageCommand.content)) {
                resourceService.deleteCmsPageContent(page.prettyUrl)
            }
            if (pageCommand.content) {
                resourceService.create(pageCommand)
            }
            return page
        }
    }

    @Override
    List<String> allowedTemplates() {
        return ['edit', 'empty', 'home', 'main', 'page']
    }

    @Override
    List<String> allowedTypes() {
        return [Constants.CmsTypes.CMS_TYPE]
    }

    @Override
    List<CmsPage> allowedPages() {
        return [new CmsPage(id:0, name:'root')] + this.pages()
    }
}

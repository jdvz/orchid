package club.orchid.service

import club.orchid.Constants
import club.orchid.dao.PageRepository
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.ContentPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import club.orchid.strategy.PageContentStrategy
import club.orchid.web.forms.PageCommand
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util.function.Supplier

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

    @Override
    List<ContentPage> contentpages() {
        return pageRepository.contentpages()
    }

    @Override
    List<Page> pages() {
        return pageRepository.pages()
    }

    @Override
    List<Page> subpages(long mainPageId) {
        return pageRepository.subpages(mainPageId)
    }

    @Override
    List<Page> chapters(long contentPageId) {
        return pageRepository.chapters(contentPageId)
    }

    @Override
    public <T extends Page<T>> Optional<T> page(String prettyUrl) {
        return pageRepository.page(prettyUrl)
    }

    @Override
    Optional <MultiCmsPage> page(String prettyUrl, int page) {
        return pageRepository.page(prettyUrl, page)
    }

    @Override
    def <T extends Page<T>> Optional<T> page(long pageId) {
        return pageRepository.page(pageId)
    }

    @Override
    String pageContent(long pageId) {
        String contentPath = pageRepository.pageContentPath(pageId)
        return contentPath ? resourceService.getCmsFullPageContent(contentPath) : ''
    }

    @Override
    public <T extends Page<T>> T save(PageCommand pageCommand) {
        PageContentStrategy<T> strategy = context.getPageContentStrategy(pageCommand.type)
        T page = pageCommand.pageId ? pageRepository.page(pageCommand.pageId).orElseThrow({
            return new IllegalArgumentException("Page not found")
        }) : strategy.createPage()
        return strategy.savePage(page, pageCommand)
    }

    @Transactional
    public CmsPage savePage(final CmsPage cmsPage, final PageCommand pageCommand) {
        try {
            if (cmsPage.isPersistent()) {
                pageRepository.update(cmsPage, pageCommand)
                if (cmsPage.prettyUrl != pageCommand.prettyUrl) {
                    resourceService.deleteCmsPageContent(cmsPage.prettyUrl)
                }
                resourceService.create(pageCommand)
                return cmsPage
            } else {
                final CmsPage page = pageRepository.create(cmsPage, pageCommand)
                resourceService.create(pageCommand)
                return page
            }
        } catch (IOException e)  {
            log.error("Can't save file $pageCommand.prettyUrl with error $e.message", e)
        }
        null
    }

    @Transactional
    public ContentPage savePage(final ContentPage contentPage, final PageCommand pageCommand) {
        try {
            if (contentPage.isPersistent()) {
                return pageRepository.update(contentPage, pageCommand)
            } else {
                return pageRepository.create(contentPage, pageCommand)
            }
        } catch (IOException e)  {
            log.error("Can't save file $pageCommand.prettyUrl with error $e.message", e)
        }
        null
    }

    @Override
    List<String> allowedTemplates() {
        return ['edit', 'empty', 'home', 'main', 'page']
    }

    @Override
    List<String> allowedTypes() {
        return [Constants.CmsTypes.CMS_TYPE, Constants.CmsTypes.CONTENT_TYPE]
    }
}

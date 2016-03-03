package club.orchid.service

import club.orchid.dao.PageRepository
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.CmsPageContent
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import club.orchid.web.forms.PageCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:32 AM
 */
@Service
class PageService implements IPageService {
    @Autowired
    private PageRepository pageRepository

    @Override
    List<Page> contentpages() {
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
    String pageContent(long pageId) {
        def content = pageRepository.pageContent(pageId)
        return content ? content.join('') : ''
    }

    @Transactional
    @Override
    Page save(final CmsPage cmsPage, final PageCommand pageCommand) {
        if (cmsPage.isPersistent()) {
            pageRepository.update(cmsPage, pageCommand)
            pageRepository.update(new CmsPageContent(content: pageCommand.content, pageId: cmsPage.id))
            return cmsPage
        } else {
            final CmsPage page = pageRepository.create(pageCommand)
            pageRepository.create(new CmsPageContent(content: pageCommand.content, pageId: page.id))
            return page
        }
    }
}

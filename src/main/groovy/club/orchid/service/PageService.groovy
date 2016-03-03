package club.orchid.service

import club.orchid.dao.PageRepository
import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.MultiCmsPage
import club.orchid.domain.cms.Page
import club.orchid.util.AuthenticationUtils
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
    Page save(CmsPage cmsPage) {
        if (cmsPage.isPersistent()) {

        } else {
            pageRepository.create(cmsPage)
            pageRepository.save()
        }
        return null
    }
}

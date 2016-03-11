package club.orchid.service

import club.orchid.domain.cms.CmsPage
import club.orchid.domain.cms.CmsPageContent
import club.orchid.domain.cms.Image
import club.orchid.web.forms.PageCommand
import org.springframework.web.multipart.MultipartFile

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 4:33 PM
 */
interface IResourceService {
    Image getOrCreateImage(final String imageName, final String prettyUrl)
    Image createImage(final String imageName)
    Image createImage(final String imageName, final String prettyUrl)
    String getCmsFullPageContent(String prettyUrl)
    boolean deleteCmsPageContent(String prettyUrl)
    Image save(final Image image, final MultipartFile file)
    void create(PageCommand pageCommand)
    Image getImageFullContentByUid(String uid)

}
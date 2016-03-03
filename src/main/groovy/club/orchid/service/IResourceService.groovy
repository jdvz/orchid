package club.orchid.service

import club.orchid.domain.cms.Image

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 4:33 PM
 */
interface IResourceService {
    Image getOrCreateImage(final String imageName, final String prettyUrl)
    Image save(final Image image)
    Optional<Image> getImageByUid(String uid)
}
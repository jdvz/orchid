package club.orchid.dao.resource

import club.orchid.dao.PersistentRepository
import club.orchid.domain.auth.User
import club.orchid.domain.cms.Image

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 4:47 PM
 */
interface ResourceDao extends PersistentRepository<Image> {
    Optional<Image> getImageByNameAndPrettyUrl(String name, String prettyUrl)
    Optional<Image> getImageByNameOrPrettyUrl(String uid)
    Image save(Image image)
}
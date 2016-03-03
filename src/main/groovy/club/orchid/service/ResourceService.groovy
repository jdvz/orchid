package club.orchid.service

import club.orchid.dao.ResourceDao
import club.orchid.domain.cms.Image
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 4:34 PM
 */
@Service
class ResourceService implements IResourceService {
    @Autowired
    ResourceDao resourceDao

    @Override
    Image getOrCreateImage(final String imageName, final String prettyUrl) {
        return resourceDao.getImageByNameAndPrettyUrl(imageName, prettyUrl)
                .orElse(new Image(
                            lazy: true,
                            name: imageName,
                            prettyUrl: prettyUrl ?: imageName,
                            realName: UUID.randomUUID(),
                            discriminator: 'Image'))
    }

    @Override
    @Transactional
    Image save(Image image) {
        return resourceDao.save(image)
    }

    @Override
    Optional<Image> getImageByUid(String uid) {
        return resourceDao.getImageByNameOrPrettyUrl(uid)
    }
}

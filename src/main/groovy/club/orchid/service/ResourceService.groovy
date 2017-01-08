package club.orchid.service

import club.orchid.Constants
import club.orchid.dao.resource.ResourceDao
import club.orchid.domain.cms.Image
import club.orchid.util.MimeUtils
import club.orchid.web.forms.PageCommand
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.FileCopyUtils
import org.springframework.web.multipart.MultipartFile

import javax.activation.MimetypesFileTypeMap
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 4:34 PM
 */
@Service
class ResourceService implements IResourceService {
    private static final Logger log = Logger.getLogger(ResourceService.class.name)
    @Autowired
    ResourceDao resourceDao
    @Autowired
    IConfigService configService
    @Value('${path.images:resources/images}')
    String pathImages
    @Value('${path.htmls:resources/html}')
    String pathHtmls

    @Override
    Image getOrCreateImage(final String imageName, final String prettyUrl, String contentType) {
        return resourceDao.getImageByNameAndPrettyUrl(imageName, prettyUrl, contentType)
                .orElseGet({ -> createImage(imageName, contentType)})
/*
                .orElse(new Image(
                            lazy: true,
                            name: imageName,
                            prettyUrl: prettyUrl ?: imageName,
                            realDir: "dir1",
                            realName: UUID.randomUUID(),
                            discriminator: 'Image'))
*/
    }

    @Override
    Image createImage(String imageName, String contentType) {
        String prettyUrl = imageName + UUID.randomUUID()
        final int filesCount = resourceDao.getNextVal(Constants.Sequences.FILE_SEQUENCE)
        final int dirIndex = filesCount == 0 ?
                configService.updateInt(Constants.Configuration.LAST_DIRECTORY_INDEX, resourceDao.getNextVal(Constants.Sequences.DIR_SEQUENCE)) :
                configService.getInt(Constants.Configuration.LAST_DIRECTORY_INDEX, 0)
        return createImage(imageName, "dir$dirIndex", prettyUrl, contentType)
    }

    @Override
    Image createImage(String imageName, String dir, String prettyUrl, String contentType) {
        final String extension = MimeUtils.extensionFromMime(contentType)
        return new Image(
                lazy: true,
                name: imageName,
                prettyUrl: prettyUrl,
                realDir: dir,
                realName: "${UUID.randomUUID()}.${extension}",
                mime: contentType,
                discriminator: 'Image')
    }

    @Override
    String getCmsFullPageContent(String prettyUrl) {
        final File file = new File("${pathHtmls}/${prettyUrl}.html")
        return file.exists() ? file.text : ''
    }

    @Override
    boolean deleteCmsPageContent(String prettyUrl) {
        final File file = new File("${pathHtmls}/${prettyUrl}.html")
        if (file.exists()) {
            file.delete()
            return true
        }
        return false
    }

    @Override
    Image getImageFullContentByUid(String uid) {
        Optional<Image> optImage = resourceDao.getImageByNameOrPrettyUrl(uid)
        if (optImage.isPresent()) {
            final Image image = optImage.get()
            File file = new File("$pathImages/${image.realDir}/${image.realName}")
            if (file.exists()) {
                image.bytes = file.bytes
                return image
            }
        }
        return null
    }

    @Override
    Collection<String> getImageNames(String dir) {
        return resourceDao.getImageNames(dir)
    }

    @Override
    @Transactional
    Image save(final Image image, final MultipartFile file) {
        try {
            Path dir = Paths.get("$pathImages/${image.realDir}")
            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File("$pathImages/${image.realDir}/${image.realName}")))
            FileCopyUtils.copy(file.getInputStream(), stream)
            stream.close()
            image.mime = file.contentType
            return resourceDao.save(image)
        } catch (IOException e) {
            log.error("Can't save file $image.realName with error $e.message", e)
        }
        return null
    }

    @Override
    void create(PageCommand pageCommand) {
        try {
            Writer writer = new FileWriter(new File("$pathHtmls/${pageCommand.prettyUrl}.html"))
            FileCopyUtils.copy(pageCommand.content, writer)
            writer.close()
        } catch (IOException e) {
            log.error("Can't save file $pageCommand.prettyUrl with error $e.message", e)
        }
    }
}

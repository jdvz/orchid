package club.orchid.util

import club.orchid.domain.cms.Image
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

/**
 *
 *
 * @author Dmitri Zaporozhtsev <dmitri.zaporozhtsev@novardis.com>
 * @package
 * @link http://www.novardis.com/
 * @copyright 2016 NOVARDIS
 */
class FrontendUtils {
    private FrontendUtils() {
    }

    public static final String createFileName(final String originalFileName) {
        originalFileName ? originalFileName.contains('.') ? originalFileName.substring(0, originalFileName.indexOf('.')) : originalFileName : 'fileName' + UUID.randomUUID()
    }

    public static final String createImageUrl(final Image image) {
        if (!image || image.lazy) {
            return '/'
        } else {
            ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/${image.prettyUrl}.${createImageExtension(image)}").build().toUriString()
        }
    }

    public static final String createImageExtension(final Image image) {
        return image.mime?.substring(image?.mime?.indexOf('/') + 1)
    }
}

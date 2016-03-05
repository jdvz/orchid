package club.orchid.domain.cms

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.CmsEntry

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 3:32 PM
 */
class Image extends CmsEntry<Image> {
    @Primitive
    String realName
    @Primitive
    String mime

    byte[] bytes

    @Override
    String getContent() {
        return content
    }
}

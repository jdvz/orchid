package club.orchid.util

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 1/8/17 10:58 PM
 */
class MimeUtils {

    public static final String MIME_TYPE_DELIMITER = '/'

    public static final String extensionFromMime(final String mimeType) {
        if (mimeType?.contains(MIME_TYPE_DELIMITER)) {
            return mimeType.substring(mimeType.indexOf(MIME_TYPE_DELIMITER) + 1)
        }
        return ""
    }
}

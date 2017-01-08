package club.orchid

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/4/16 6:12 PM
 */
class Constants {
    private Constants() {
    }

    public static class CmsTypes {
        private CmsTypes() {
        }

        public static final String CONTENT_TYPE = 'ContentPage'
        public static final String CMS_TYPE = 'CmsPage'
    }

    public static class Sequences {
        private Sequences() {
        }

        public static final String DIR_SEQUENCE = 'directory_sequence'
        public static final String FILE_SEQUENCE = 'files_sequence'
    }

    public static class Configuration {
        private Configuration() {
        }

        public static final String LAST_DIRECTORY_INDEX = 'last_dir'
    }
}

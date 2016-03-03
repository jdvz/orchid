package club.orchid.domain.cms

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/3/16 12:43 AM
 */
class ContentPage extends Page<ContentPage> {
    List<Page> pages

    @Override
    String getContent() {
        return ''
    }
}

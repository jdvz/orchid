package club.orchid.util

import club.orchid.domain.auth.User
import club.orchid.domain.cms.CmsPage
/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 3/2/16 8:35 PM
 */
class AuthenticationUtils {
    public static final String ANONYMOUS_NAME = 'anonymous'

    public static final User anonymous = new User(
            email: ANONYMOUS_NAME,
            anonymous: true,
            accountNonExpired: true,
            accountNonLocked: true,
            credentialsNonExpired: true,
            enabled: true,
            roles: 'ROLE_ANONYMOUS'
    )

    public static final CmsPage page404 = new CmsPage(content: '404', template: 'system', pageName: '404')

    private AuthenticationUtils() {
    }
}

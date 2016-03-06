package club.orchid.domain.auth

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.AbstractMappedPersistent
import groovy.transform.Canonical
import org.springframework.security.core.GrantedAuthority

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 5:47 PM
 */
@Canonical
class Role extends AbstractMappedPersistent<Role> implements GrantedAuthority {
    public static final long serialVersionUID = 1l
    @Primitive
    String roleName

    @Override
    transient String getAuthority() {
        return roleName
    }
}

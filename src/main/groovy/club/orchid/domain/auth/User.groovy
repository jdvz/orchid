package club.orchid.domain.auth

import club.orchid.anno.mapping.Primitive
import club.orchid.domain.AbstractMappedPersistent
import groovy.transform.Canonical
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Created with IntelliJ IDEA.
 * @author: zera
 * @date: 2/14/16 4:27 PM
 */
@Canonical
class User extends AbstractMappedPersistent<User> implements UserDetails {
    public static final long serialVersionUID = 1l
    @Primitive
    String firstName
    @Primitive
    String lastName
    @Primitive
    String password
    @Primitive
    String email
    @Primitive
    boolean accountNonExpired
    @Primitive
    boolean accountNonLocked
    @Primitive
    boolean credentialsNonExpired
    @Primitive
    boolean enabled

    transient boolean anonymous
    transient boolean clearAuthorization

    String roles

    transient Collection<? extends GrantedAuthority>authorities

    @Override
    String getUsername() {
        return email
    }

    String getFullName() {
        [firstName, lastName].findAll { it }.join(' ')
    }

    Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities ?: roles?.split(',')?.collect { new Role(roleName:it) } ?: Collections.emptyList()
    }
}

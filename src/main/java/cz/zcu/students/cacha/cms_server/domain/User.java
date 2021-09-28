package cz.zcu.students.cacha.cms_server.domain;

import cz.zcu.students.cacha.cms_server.validators.UniqueUsername;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.*;

@Entity
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Jméno nesmí být prázdné")
    @Size(min = 3, max = 255, message = "Jméno musí mít minimálně 3 znaky a maximálně 255 znaků")
    @UniqueUsername
    private String username;

    @NotNull(message = "Heslo nesmí být prázdné")
    @Size(min = 8, max = 255, message = "Heslo musí mít minimálně 8 znaků a maximálně 255 znaků")
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message="Heslo musí obshovat alespoň jedno malé písmeno, velké písmeno a číslo")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private Boolean banned;
    private Boolean deleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Article> articles;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate()
    {
        createdAt = new Date();
        banned = false;
        deleted = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !banned && !deleted;
    }

    public boolean isAuthor() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_AUTHOR);
    }

    public boolean isReviewer() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_REVIEWER);
    }

    public boolean isAdmin() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toList()).contains(ROLE_ADMIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

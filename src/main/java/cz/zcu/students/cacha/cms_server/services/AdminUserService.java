package cz.zcu.students.cacha.cms_server.services;

import cz.zcu.students.cacha.cms_server.domain.Role;
import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.exceptions.NotFoundException;
import cz.zcu.students.cacha.cms_server.exceptions.UnauthorizedException;
import cz.zcu.students.cacha.cms_server.repositories.RoleRepository;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import cz.zcu.students.cacha.cms_server.view_models.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_AUTHOR;
import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_REVIEWER;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Set<UserVM> getNonAdminUsers() {
        Set<User> users = userRepository.getNonAdminUsers();
        return users.stream().map(UserVM::new).collect(Collectors.toSet());
    }

    public void setAuthor(Long id, Boolean value) {
        User user = getValidUser(id);

        boolean isAuthor = user.isAuthor();
        if((isAuthor && value) || (!isAuthor && !value)) {
            return;
        }

        if(value) {
            Role role = roleRepository.findByName(ROLE_AUTHOR);
            user.getRoles().add(role);
        } else {
            user.setRoles(user.getRoles().stream().filter(role -> !role.getName().equals(ROLE_AUTHOR)).collect(Collectors.toSet()));
        }

        userRepository.save(user);
    }

    public void setReviewer(Long id, Boolean value) {
        User user = getValidUser(id);

        boolean isReviewer = user.isReviewer();
        if((isReviewer && value) || (!isReviewer && !value)) {
            return;
        }

        if(value) {
            Role role = roleRepository.findByName(ROLE_REVIEWER);
            user.getRoles().add(role);
        } else {
            user.setRoles(user.getRoles().stream().filter(role -> !role.getName().equals(ROLE_REVIEWER)).collect(Collectors.toSet()));
        }

        userRepository.save(user);
    }

    public void setBan(Long id, Boolean value) {
        User user = getValidUser(id);
        user.setBanned(value);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getValidUser(id);
        user.setDeleted(true);
        user.setBanned(true);
        userRepository.save(user);
    }

    private User getValidUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("Uživatel nebyl nalezen");
        }

        User user = userOptional.get();

        if(user.isAdmin()) {
            throw new UnauthorizedException("Akce nemůže být vykonána pro admina");
        }

        return user;
    }
}

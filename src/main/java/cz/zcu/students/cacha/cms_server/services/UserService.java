package cz.zcu.students.cacha.cms_server.services;

import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.repositories.RoleRepository;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import cz.zcu.students.cacha.cms_server.responses.JWTLoginSucessResponse;
import cz.zcu.students.cacha.cms_server.security.JwtTokenProvider;
import cz.zcu.students.cacha.cms_server.view_models.UsernamePasswordVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static cz.zcu.students.cacha.cms_server.security.SecurityConstants.TOKEN_PREFIX;
import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_AUTHOR;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleRepository.findByName(ROLE_AUTHOR));
        userRepository.save(user);
    }

    public JWTLoginSucessResponse login(UsernamePasswordVM usernamePasswordVM) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usernamePasswordVM.getUsername(),
                        usernamePasswordVM.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return new JWTLoginSucessResponse(true, jwt);
    }

    public String getFreshToken() {
        return TOKEN_PREFIX + tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());
    }

    public void updateName(User user, String username) {
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateName(User user, String username, String password) {
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}

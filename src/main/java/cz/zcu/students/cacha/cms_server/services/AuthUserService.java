package cz.zcu.students.cacha.cms_server.services;

import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);
        if(user == null) {
            throw new UsernameNotFoundException("Uživatel nebyl nalezen");
        }
        return user;
    }

    public User loadUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty())
        {
            throw new UsernameNotFoundException("Uživatel nebyl nalezen");
        }
        return userOptional.get();
    }
}
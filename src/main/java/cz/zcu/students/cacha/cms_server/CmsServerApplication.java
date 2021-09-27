package cz.zcu.students.cacha.cms_server;

import cz.zcu.students.cacha.cms_server.domain.Role;
import cz.zcu.students.cacha.cms_server.repositories.RoleRepository;
import cz.zcu.students.cacha.cms_server.security.JwtAuthenticationFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.*;

@SpringBootApplication
public class CmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmsServerApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args -> {
            long count = roleRepository.count();

            if(count == 0) {
                Role author = new Role(ROLE_AUTHOR);
                roleRepository.save(author);

                Role reviewer = new Role(ROLE_REVIEWER);
                roleRepository.save(reviewer);

                Role admin = new Role(ROLE_ADMIN);
                roleRepository.save(admin);
            }
        };
    }
}

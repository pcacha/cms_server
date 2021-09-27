package cz.zcu.students.cacha.cms_server.repositories;

import cz.zcu.students.cacha.cms_server.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

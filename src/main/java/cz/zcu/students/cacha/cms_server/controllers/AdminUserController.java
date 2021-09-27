package cz.zcu.students.cacha.cms_server.controllers;

import cz.zcu.students.cacha.cms_server.responses.GenericResponse;
import cz.zcu.students.cacha.cms_server.services.AdminUserService;
import cz.zcu.students.cacha.cms_server.view_models.BooleanValVM;
import cz.zcu.students.cacha.cms_server.view_models.UserVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_ADMIN;

@RestController
@RequestMapping("/api/usermanager")
@Secured(ROLE_ADMIN)
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping
    public List<UserVM> getNonAdminUsers() {
        List<UserVM> users = adminUserService.getNonAdminUsers();
        return users;
    }

    @PutMapping("/author/{id}")
    public GenericResponse setAuthor(@Valid @RequestBody BooleanValVM value, @PathVariable Long id) {
        adminUserService.setAuthor(id, value.getValue());
        return new GenericResponse("Autorská práva byla nastavena");
    }

    @PutMapping("/reviewer/{id}")
    public GenericResponse setReviewer(@Valid @RequestBody BooleanValVM value, @PathVariable Long id) {
        adminUserService.setReviewer(id, value.getValue());
        return new GenericResponse("Recenzentská práva byla nastavena");
    }

    @PutMapping("/ban/{id}")
    public GenericResponse setBan(@Valid @RequestBody BooleanValVM value, @PathVariable Long id) {
        adminUserService.setBan(id, value.getValue());
        return new GenericResponse("Uživateli byl aktualizován ban");
    }

    @DeleteMapping("/delete/{id}")
    public GenericResponse deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return new GenericResponse("Uživatel byl smazán");
    }
}

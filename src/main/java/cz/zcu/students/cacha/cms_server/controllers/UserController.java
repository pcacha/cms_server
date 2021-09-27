package cz.zcu.students.cacha.cms_server.controllers;

import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.responses.GenericResponse;
import cz.zcu.students.cacha.cms_server.responses.JWTLoginSucessResponse;
import cz.zcu.students.cacha.cms_server.services.UserService;
import cz.zcu.students.cacha.cms_server.shared.CurrentUser;
import cz.zcu.students.cacha.cms_server.view_models.UpdateNameVM;
import cz.zcu.students.cacha.cms_server.view_models.UsernamePasswordUpdateVM;
import cz.zcu.students.cacha.cms_server.view_models.UsernamePasswordVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_AUTHOR;
import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_REVIEWER;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponse register(@Valid @RequestBody User user) {
        userService.save(user);
        return new GenericResponse("Uživatel zaregistrován");
    }

    @PostMapping("/login")
    public JWTLoginSucessResponse login(@Valid @RequestBody UsernamePasswordVM usernamePasswordVM) {
        JWTLoginSucessResponse response = userService.login(usernamePasswordVM);
        return response;
    }

    @GetMapping("/token")
    @Secured({ROLE_AUTHOR, ROLE_REVIEWER})
    public JWTLoginSucessResponse token() {
        String jwt = userService.getFreshToken();
        return new JWTLoginSucessResponse(true, jwt);
    }

    @PostMapping("/updateName")
    @Secured({ROLE_AUTHOR, ROLE_REVIEWER})
    public GenericResponse updateName(@Valid @RequestBody UpdateNameVM updateNameVM, @CurrentUser User user) {
        userService.updateName(user, updateNameVM.getUsername());
        return new GenericResponse("Jméno bylo aktualizováno");
    }

    @PostMapping("/updateUser")
    @Secured({ROLE_AUTHOR, ROLE_REVIEWER})
    public GenericResponse updateName(@Valid @RequestBody UsernamePasswordUpdateVM sernamePasswordUpdateVM, @CurrentUser User user) {
        userService.updateName(user, sernamePasswordUpdateVM.getUsername(), sernamePasswordUpdateVM.getPassword());
        return new GenericResponse("Uživatel byl aktualizován");
    }
}

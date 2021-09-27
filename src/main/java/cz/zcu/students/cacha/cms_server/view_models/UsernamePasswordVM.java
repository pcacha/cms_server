package cz.zcu.students.cacha.cms_server.view_models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsernamePasswordVM {
    @NotNull(message = "Jméno nesmí být prázdné")
    private String username;
    @NotNull(message = "Heslo nesmí být prázdné")
    private String password;
}

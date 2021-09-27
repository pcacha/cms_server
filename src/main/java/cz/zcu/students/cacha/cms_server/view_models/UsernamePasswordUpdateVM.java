package cz.zcu.students.cacha.cms_server.view_models;

import cz.zcu.students.cacha.cms_server.validators.UniqueUsernameExclPrincipal;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UsernamePasswordUpdateVM {
    @NotNull(message = "Jméno nesmí být prázdné")
    @Size(min = 3, max = 255, message = "Jméno musí mít minimálně 3 znaky a maximálně 255 znaků")
    @UniqueUsernameExclPrincipal
    private String username;
    @NotNull(message = "Heslo nesmí být prázdné")
    @Size(min = 8, max = 255, message = "Heslo musí mít minimálně 8 znaků a maximálně 255 znaků")
    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message="Heslo musí obshovat alespoň jedno malé písmeno, velké písmeno a číslo")
    private String password;
}
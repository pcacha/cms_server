package cz.zcu.students.cacha.cms_server.view_models;

import cz.zcu.students.cacha.cms_server.validators.UniqueUsernameExclPrincipal;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdateNameVM {
    @NotNull(message = "Jméno nesmí být prázdné")
    @Size(min = 3, max = 255, message = "Jméno musí mít minimálně 3 znaky a maximálně 255 znaků")
    @UniqueUsernameExclPrincipal
    private String username;
}

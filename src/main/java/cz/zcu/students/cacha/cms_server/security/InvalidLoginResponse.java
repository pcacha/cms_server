package cz.zcu.students.cacha.cms_server.security;

import lombok.Data;

@Data
public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Chybné jméno";
        this.password = "Chybné heslo";
    }
}

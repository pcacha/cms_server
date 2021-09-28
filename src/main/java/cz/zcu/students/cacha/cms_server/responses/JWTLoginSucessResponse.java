package cz.zcu.students.cacha.cms_server.responses;

import lombok.Data;

@Data
public class JWTLoginSucessResponse {
    private boolean success;
    private String token;

    public JWTLoginSucessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }
}
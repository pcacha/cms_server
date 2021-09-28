package cz.zcu.students.cacha.cms_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.cms_server.domain.Role;
import cz.zcu.students.cacha.cms_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_AUTHOR;
import static cz.zcu.students.cacha.cms_server.shared.RolesConstants.ROLE_REVIEWER;

@Data
@NoArgsConstructor
public class UserVM {
    private Long id;
    private String username;
    @JsonFormat(pattern = "dd.M. yyyy")
    private Date createdAt;
    private Boolean isAuthor;
    private Boolean isReviewer;
    private Boolean isBanned;

    public UserVM(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();

        this.isAuthor = false;
        this.isReviewer = false;
        for(Role role : user.getRoles()) {
            if(role.getName().equalsIgnoreCase(ROLE_AUTHOR)) {
                this.isAuthor = true;
            }
            else if (role.getName().equalsIgnoreCase(ROLE_REVIEWER)) {
                this.isReviewer = true;
            }
        }

        this.isBanned = user.getBanned();
    }
}

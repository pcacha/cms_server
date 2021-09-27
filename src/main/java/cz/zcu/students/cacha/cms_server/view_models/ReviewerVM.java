package cz.zcu.students.cacha.cms_server.view_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.zcu.students.cacha.cms_server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReviewerVM {
    private Long id;
    private String username;
    @JsonFormat(pattern = "dd.MM. yyyy")
    private Date createdAt;

    public ReviewerVM(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.createdAt = user.getCreatedAt();
    }
}

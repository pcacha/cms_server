package cz.zcu.students.cacha.cms_server.view_models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class BooleanValVM {
    @NotNull
    private Boolean value;
}

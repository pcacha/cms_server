package cz.zcu.students.cacha.cms_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue
    @NotNull(message = "Id nesmí být prázdné")
    private Long id;

    @NotNull(message = "Hodnocení nesmí být prázdné")
    @Size(min = 10, max = 5000, message = "Hodnocení musí mít minimálně 10 znaků a maximálně 5000 znaků")
    @Column(length = 5000)
    private String evaluation;

    @NotNull(message = "Počet hvězd nesmí být prázdný")
    @Min(value = 1, message = "Počet hvězd musí být minimálně 1")
    @Max(value = 5, message = "Počet hvězd musí být maximálně 5")
    private Integer starCount;

    @NotNull(message = "Doporučení nesmí být prázdné")
    private Boolean recommended;

    @ManyToOne(fetch=FetchType.EAGER)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch=FetchType.EAGER)
    @JsonIgnore
    private Article article;
}

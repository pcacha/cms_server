package cz.zcu.students.cacha.cms_server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import cz.zcu.students.cacha.cms_server.validators.PdfFile;
import cz.zcu.students.cacha.cms_server.validators.UniqueArticleName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "Název nesmí být prázdný")
    @Size(min = 3, max = 255, message = "Název musí mít minimálně 3 znaky a maximálně 255 znaků")
    @UniqueArticleName
    private String name;

    @NotNull(message = "Text nesmí být prázdný")
    @Size(min = 10, max = 10000, message = "Abstrakt článku nemá správnou délku")
    @Column(length = 10000)
    private String text;

    @NotNull(message = "Název přílohy nesmí být prázdný")
    private String documentName;

    @Transient
    @NotNull(message = "Příloha nesmí být prázdná")
    @PdfFile
    private String encodedDocument;

    @ManyToOne(fetch=FetchType.EAGER)
    @JsonIgnore
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd.mm. yyyy")
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private ArticleState state;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd.mm. yyyy")
    private Date publishedAt;

    private String evaluation;

    @OneToMany(mappedBy = "article", fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Review> reviews;

    @PrePersist
    protected void onCreate()
    {
        createdAt = new Date();
        state = ArticleState.REVIEWED;
    }
}

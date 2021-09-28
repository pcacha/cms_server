package cz.zcu.students.cacha.cms_server.domain;

import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import cz.zcu.students.cacha.cms_server.validators.PdfFile;
import cz.zcu.students.cacha.cms_server.validators.UniqueArticleName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private ArticleState state;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    private String evaluation;

    @OneToMany(mappedBy = "article", fetch=FetchType.LAZY)
    private Set<Review> reviews;

    @PrePersist
    protected void onCreate()
    {
        createdAt = new Date();
        state = ArticleState.REVIEWED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

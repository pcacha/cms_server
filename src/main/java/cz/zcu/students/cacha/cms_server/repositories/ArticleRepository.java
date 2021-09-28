package cz.zcu.students.cacha.cms_server.repositories;

import cz.zcu.students.cacha.cms_server.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findByName(String name);
    Set<Article> findByPublishedAtIsNotNullOrderByPublishedAtDesc();
    @Query(
            value = "select * from article " +
                    "where user_id = :user_id " +
                    "order by created_at desc",
            nativeQuery = true
    )
    Set<Article> findPersonalArticles(@Param("user_id") Long user_id);
    Set<Article> findByEvaluationIsNull();
}

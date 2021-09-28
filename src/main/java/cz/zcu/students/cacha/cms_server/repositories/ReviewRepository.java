package cz.zcu.students.cacha.cms_server.repositories;

import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByUserAndEvaluationIsNull(User user);
    Set<Review> findByUserAndEvaluationIsNotNull(User user);
    @Query(
            value = "select * from review r " +
                    "where article_id = :article_id and " +
                    "evaluation is not null",
            nativeQuery = true
    )
    Set<Review> findByArticleId(@Param("article_id") Long article_id);
}

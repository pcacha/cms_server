package cz.zcu.students.cacha.cms_server.repositories;

import cz.zcu.students.cacha.cms_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String userName);
    @Query(
            value = "select * from user u " +
                    "where u.deleted = 0 and " +
                    "'ROLE_ADMIN' not in (" +
                    "select r.name from role r " +
                    "join users_roles ur on r.id = ur.role_id " +
                    "where ur.user_id = u.id);",
            nativeQuery = true)
    Set<User> getNonAdminUsers();
    @Query(
            value = "select * from user us " +
                    "where us.id not in ( " +
                    "select u.id from user u " +
                    "join review r on r.user_id = u.id " +
                    "join article a on r.article_id = a.id " +
                    "where a.id = :article_id ) " +
                    "and us.id != :author_id",
            nativeQuery = true
    )
    Set<User> selectNotReviewers(@Param("article_id") Long article_id, @Param("author_id") Long author_id);
    @Query(
            value = "select * from user u " +
                    "join review r on r.user_id = u.id " +
                    "join article a on r.article_id = a.id " +
                    "where a.id = :article_id",
            nativeQuery = true
    )
    Set<User> selectReviewers(@Param("article_id") Long article_id);
}

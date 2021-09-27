package cz.zcu.students.cacha.cms_server.services;

import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.exceptions.CannotUpdateReviewException;
import cz.zcu.students.cacha.cms_server.exceptions.NotFoundException;
import cz.zcu.students.cacha.cms_server.exceptions.UnauthorizedException;
import cz.zcu.students.cacha.cms_server.repositories.ReviewRepository;
import cz.zcu.students.cacha.cms_server.shared.ArticleState;
import cz.zcu.students.cacha.cms_server.view_models.ReviewArticleVM;
import cz.zcu.students.cacha.cms_server.view_models.ReviewVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ReviewArticleVM> getAssigned(User user) {
        List<Review> reviews = reviewRepository.findByUserAndEvaluationIsNull(user);
        reviews = reviews.stream().filter(review -> review.getArticle().getState() == ArticleState.REVIEWED).collect(Collectors.toList());
        return reviews.stream().map(review -> new ReviewArticleVM(review.getArticle(), review)).collect(Collectors.toList());
    }

    public void updateOrCreateReview(User user, Review review) {
        Optional<Review> reviewInDBOptional = reviewRepository.findById(review.getId());

        if(reviewInDBOptional.isEmpty()) {
            throw new NotFoundException("Hodnocení nebylo nalezeno");
        }

        Review reviewInDB = reviewInDBOptional.get();

        if(reviewInDB.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("Hodnocení není přiřazeno přihlášenému uživateli");
        }

        if(reviewInDB.getArticle().getState() != ArticleState.REVIEWED) {
            throw new CannotUpdateReviewException("Lze měnit obsah rezenzí pouze aktuálně hodnocených článků");
        }

        reviewInDB.setEvaluation(review.getEvaluation());
        reviewInDB.setStarCount(review.getStarCount());
        reviewInDB.setRecommended(review.getRecommended());
        reviewRepository.save(reviewInDB);
    }

    public List<ReviewVM> getMyReviews(User user) {
        List<Review> reviews = reviewRepository.findByUserAndEvaluationIsNotNull(user);
        return reviews.stream().map(ReviewVM::new).collect(Collectors.toList());
    }

    public ReviewVM getReview(Long reviewId, User user) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

        if(reviewOptional.isEmpty()) {
            throw new NotFoundException("Hodnocení nebylo nalezeno");
        }

        Review review = reviewOptional.get();

        if(review.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("Hodnocení není přiřazeno přihlášenému uživateli");
        }

        return new ReviewVM(review);
    }
}

package cz.zcu.students.cacha.cms_server.controllers;

import cz.zcu.students.cacha.cms_server.domain.Review;
import cz.zcu.students.cacha.cms_server.domain.User;
import cz.zcu.students.cacha.cms_server.responses.GenericResponse;
import cz.zcu.students.cacha.cms_server.services.ReviewService;
import cz.zcu.students.cacha.cms_server.shared.CurrentUser;
import cz.zcu.students.cacha.cms_server.view_models.ReviewArticleVM;
import cz.zcu.students.cacha.cms_server.view_models.ReviewVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/assigned")
    public Set<ReviewArticleVM> getAssignedReviews(@CurrentUser User user) {
        Set<ReviewArticleVM> assigned = reviewService.getAssigned(user);
        return assigned;
    }

    @PostMapping()
    public GenericResponse updateOrCreateReview(@CurrentUser User user, @Valid @RequestBody Review review) {
        reviewService.updateOrCreateReview(user, review);
        return new GenericResponse("Recenze byla přijata");
    }

    @GetMapping
    public Set<ReviewVM> getMyReviews(@CurrentUser User user) {
        Set<ReviewVM> myReviews = reviewService.getMyReviews(user);
        return myReviews;
    }

    @GetMapping("/{reviewId}")
    public ReviewVM getReview(@PathVariable Long reviewId, @CurrentUser User user) {
        ReviewVM review = reviewService.getReview(reviewId, user);
        return review;
    }
}

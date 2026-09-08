package org.example.pensionatkademina.controller;

import org.example.pensionatkademina.client.ReviewClient;
import org.example.pensionatkademina.dto.ReviewDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class ReviewController {

    private final ReviewClient reviewClient;

    public ReviewController(final ReviewClient reviewClient) {
        this.reviewClient = reviewClient;

    }

    @GetMapping("/reviews")
    public String reviewsPage(Model model) {
        model.addAttribute("reviews", reviewClient.getAllReviews());
        return "reviews";
    }

    @PostMapping("/reviews")
    public String reviewsPost(@ModelAttribute("reviewDto") ReviewDto reviewDto, Model model) {
        try {
            reviewClient.createReview(reviewDto);
            model.addAttribute("successMessage", "Review has been sent! thank you for your visit!");
        }
        catch(Exception e) {
            model.addAttribute("errorMessage", "you can only review a room you have booked.");
        }

        model.addAttribute("reviews", reviewClient.getAllReviews());

        return "reviews";
    }

}
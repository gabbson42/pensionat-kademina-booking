package org.example.pensionatkademina.controller;

import org.example.pensionatkademina.client.ReviewClient;
import org.example.pensionatkademina.dto.ReviewDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {

    private final ReviewClient reviewClient;
    public ReviewController(final ReviewClient reviewClient) {
        this.reviewClient = reviewClient;

    }

    @GetMapping("/reviews")
    public String reviewsPage() {
        return "reviews";
    }

    @PostMapping("/reviews")
    public String reviewsPost(@ModelAttribute("reviewDto") ReviewDto reviewDto) {
        return "redirect:/reviews";
    }


}
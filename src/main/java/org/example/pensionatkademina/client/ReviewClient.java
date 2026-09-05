package org.example.pensionatkademina.client;

import org.example.pensionatkademina.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ReviewClient {

    private final RestClient restClient;
    public ReviewClient(@Qualifier("reviewRestClient") final RestClient restClient) {
        this.restClient = restClient;
    }

    public void createReview(final ReviewDto reviewDto) {
        restClient.post()
                .uri("/api/reviews")
                .body(reviewDto)
                .retrieve()
                .toBodilessEntity();

    }

}

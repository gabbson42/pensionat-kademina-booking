package org.example.pensionatkademina.utility;

import lombok.RequiredArgsConstructor;
import org.example.pensionatkademina.client.CustomerClient;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class KademinaIndicator implements HealthIndicator {

    private final CustomerClient customerClient;

    @Override
    public Health health(){
    try{
        customerClient.checkStatus();
        return Health
                .up()
                .withDetail("API","Customer Service is up")
                .build();
    }catch(ResourceAccessException | RestClientResponseException e){
        return Health
                .down()
                .withDetail("API", "Customer Service is down")
                .build();
    }

    }


}

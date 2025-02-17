package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseRecordDto;
import com.ead.authuser.dtos.ResponsePageDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@Log4j2
public class CourseClient {

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    //@Retry(name = "retryInstance", fallbackMethod = "retryFallback")
    @CircuitBreaker(name = "circuitbreakerInstance")
    public Page<CourseRecordDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {

        String url = baseUrlCourse + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
        log.debug("Request URL: {}", url);

        try {

            return restClient.get()
                    .uri(url)
                    .header("Authorization", token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<ResponsePageDto<CourseRecordDto>>() {
                    });

        } catch (HttpStatusCodeException e) {
            log.error("Error Request RestClient with status: {}, cause: {}", e.getStatusCode(), e.getMessage());
            switch (e.getStatusCode()) {
                case FORBIDDEN -> throw new AccessDeniedException("Forbidden");
                default -> throw new RuntimeException("Error Request RestClient", e);
            }
        } catch (RestClientException e) {
            log.error("Error Request RestClient with cause: {}", e.getMessage());
            throw new RuntimeException("Error Request RestClient", e);
        }
    }

    public Page<CourseRecordDto> retryFallback (UUID userId, Pageable pageable,Throwable t) {
        log.error("Inside every retryFallback, cause - {}", t.toString());
        List<CourseRecordDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    //metodo fallback de exemplo que pode ser adicionado ao circuitbreaker
    public Page<CourseRecordDto> circuitbreakerFallback (UUID userId, Pageable pageable,Throwable t) {
        log.error("Inside every circuit breaker fallback, cause - {}", t.toString());
        List<CourseRecordDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }


}

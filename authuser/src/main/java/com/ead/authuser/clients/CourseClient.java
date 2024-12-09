package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseRecordDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
@Log4j2
public class CourseClient {

    @Value("${ead.api.url.course}")
    String baseUrlCourse;

    final RestClient restClient;

    public CourseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public Page<CourseRecordDto> getAllCoursesByUser(UUID userId, Pageable pageable) {

        String url = baseUrlCourse + "/courses?userId=" + userId + "$page=" + pageable.getPageNumber() + "$size="
                + pageable.getPageSize() + "$sort=" + pageable.getSort().toString().replaceAll(": ", ",");

        try {

        } catch (RestClientException e) {
            log.error("Error Request RestClient with cause: {}", e.getMessage());
            throw new RuntimeException("Error Request RestClient", e);
        }

    }

}

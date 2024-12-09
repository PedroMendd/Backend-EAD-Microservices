package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponsePageDto<T> extends PageImpl<T> {

    private final PageMetadata page;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDto(@JsonProperty("content") List<T> content,
                           @JsonProperty("page") PageMetadata page) {
        super(content, PageRequest.of(page.getNumber(), page.getSize()), page.getTotalElements());
        this.page = page;
    }

    public PageMetadata getPage() {
        return page;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageMetadata {

        private final int size;
        private final long totalElements;
        private final int totalPage;
        private final int number;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public PageMetadata(@JsonProperty("size") int size,
                            @JsonProperty("totalElements") int totalElements,
                            @JsonProperty("totalPage") int totalPage,
                            @JsonProperty("number") int number) {
            this.size = size;
            this.totalElements = totalElements;
            this.totalPage = totalPage;
            this.number = number;
        }

        public int getSize() {
            return size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public int getNumber() {
            return number;
        }
    }

}
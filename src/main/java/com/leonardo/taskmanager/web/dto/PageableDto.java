package com.leonardo.taskmanager.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageableDto {
    private List content = new ArrayList<>();
    private boolean first;
    private boolean last;

    @JsonProperty("page")
    private int number;

    private int size;

    @JsonProperty("page_elements")
    private int numberOfElements;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("total_elements")
    private int totalElements;
}

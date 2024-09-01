package com.leonardo.taskmanager.web.dto.mapper;

import com.leonardo.taskmanager.web.dto.PageableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class PageableMapper {
    public static PageableDto pageableDto(Page page){
        return new ModelMapper().map(page, PageableDto.class);
    }

}

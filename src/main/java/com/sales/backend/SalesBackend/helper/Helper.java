package com.sales.backend.SalesBackend.helper;

import com.sales.backend.SalesBackend.dtos.PageableResponse;
import com.sales.backend.SalesBackend.dtos.UserDto;
import com.sales.backend.SalesBackend.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type) {

        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());
        // we make this code reusable
        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setLastPage(page.isLast());
        response.setTotalPages(page.getTotalPages());

        return response;

    }
}

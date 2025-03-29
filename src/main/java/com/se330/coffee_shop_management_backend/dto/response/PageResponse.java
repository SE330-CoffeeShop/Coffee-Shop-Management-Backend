package com.se330.coffee_shop_management_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private long totalPages;
    private int currentPage;
    private int pageSize;
}

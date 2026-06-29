package com.example.mini_trade_app.shared.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ListingParams(
    Integer limit,
    Integer page
) {
    public <T> Pageable getPageable() {
        return PageRequest.of(page(), limit());
    }

    public <T> Pageable getPageable(Sort sort) {
        return PageRequest.of(page(), limit(), sort);
    }
}

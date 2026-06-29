package com.example.mini_trade_app.shared.dto;

import org.springframework.data.domain.Page;

public record Listing(
    long total,
    int  page,
    int  totalPages,
    int  size 
) {
    public static <T> Listing from(Page<T> page) {
        return new Listing(
            page.getTotalElements(), page.getNumber(),
            page.getTotalPages(), page.getSize()
        );
    }
}

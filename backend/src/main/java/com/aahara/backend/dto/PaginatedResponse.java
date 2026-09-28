package com.aahara.backend.dto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class PaginatedResponse<T> {
    private List<T> items;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}

package com.lincentpega.personalcrmjava.controller.common;

import java.util.List;

public record PagedResponseWithTotalCount<T>(
        List<T> items,
        long totalCount,
        int pageNumber,
        int pageSize
) {
}

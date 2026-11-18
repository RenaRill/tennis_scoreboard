package com.tennisscoreboard.util;

public final class Pagination {
    public static final int PAGE_SIZE = 3;

    public static int countTotalPages(long matchCount) {
        return (int) ((matchCount + PAGE_SIZE - 1) / PAGE_SIZE);
    }

    public static int countOffset(int page) {
        return (page - 1) * PAGE_SIZE;
    }
}

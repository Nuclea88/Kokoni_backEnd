package com.example.kokoni.dto.response;

import java.util.List;

public record UserCustomMediaDTOResponse(
        Long id,
        String title,
        String imageUrl,
        String customAuthor,
        Integer customTotalChapters,
        String customStatus,
        String description,
        Boolean isAddedInTracker,
    Long trackerId,
    List<ChapterProgressResponse> readChapters,
    Integer currentChapter
) {
}

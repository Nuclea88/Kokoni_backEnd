package com.example.kokoni.dto.response;

import java.util.List;
import java.util.Map;

public record UserCustomMediaDTOResponse(
        Long id,
        String title,
        String imageUrl,
        String customAuthor,
        Integer customTotalChapters,
        String customStatus,
        Map<String, String> description,
        Boolean isAddedInTracker,
        Long trackerId,
        String userStatus,
        List<ChapterProgressResponse> readChapters,
        Integer currentChapter
) {
}

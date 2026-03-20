package com.example.kokoni.service;

import java.util.List;
import com.example.kokoni.dto.request.AddChapterProgressRequest;
import com.example.kokoni.dto.response.ChapterProgressResponse;

public interface UserChapterProgressService {

    ChapterProgressResponse markChapterAsRead(Long trackerId, AddChapterProgressRequest request);

    List<ChapterProgressResponse> getReadChapters(Long trackerId);

    Integer getCurrentChapterForManga(Long mediaId);

    void unmarkChapterAsRead(Long trackerId, Integer progressUnit);

}

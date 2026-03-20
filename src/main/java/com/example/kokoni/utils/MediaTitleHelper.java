package com.example.kokoni.utils;
import org.springframework.stereotype.Component;

import com.example.kokoni.entity.MediaTitle;
import java.util.List;
import com.example.kokoni.entity.Media;

@Component
public class MediaTitleHelper {

     public String extractTitleFromMedia(Media media) {
        if (media == null) return "Sin título";
        return extractTitleFromList(media.getTitles());
    }

    public String extractTitleFromList(List<MediaTitle> titles) {
        if (titles == null || titles.isEmpty()) return "Sin título";
        return titles.stream()
            .filter(t -> t != null && Boolean.TRUE.equals(t.getIsPrimary()))
            .map(MediaTitle::getTitle)
            .findFirst()
            .orElse(titles.get(0).getTitle());
    }
    
}


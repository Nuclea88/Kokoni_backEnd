package com.example.kokoni.utils;

import org.hibernate.Hibernate;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import com.example.kokoni.entity.Manga;
import com.example.kokoni.entity.Media;
import com.example.kokoni.entity.UserChapterProgress;
import com.example.kokoni.entity.UserCustomMedia;
import com.example.kokoni.entity.UserMediaTracker;


@Component
public class MediaMetadataHelper {

    @Named("mapExternalId")
    public String mapExternalId(Media media) {
        if (media == null) return null;
        return media.getExternalId() != null ? media.getExternalId() : media.getId().toString();
    }

    public Integer calculateCurrentChapter(UserMediaTracker tracker) {
        if (tracker == null || tracker.getProgresses() == null || tracker.getProgresses().isEmpty()) return 0;
        return tracker.getProgresses().stream()
                .mapToInt(UserChapterProgress::getProgressUnit)
                .max()
                .orElse(0);
    }
    @Named("calculateTotalChapters")
    public Integer calculateTotalChapters(Media media) {
        if (media == null) return 0;

        Object unproxied = Hibernate.unproxy(media);
        if (unproxied instanceof Manga) {
            return ((Manga) unproxied).getTotalChapters();
        } else if (unproxied instanceof UserCustomMedia) {
            return ((UserCustomMedia) unproxied).getCustomTotalChapters();
        }
        return 0;
    }
}

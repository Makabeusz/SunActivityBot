package com.sojka.sunactivity.social.feed.post;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class FacebookPost implements SocialMediaPost {

    private final String title;
    private final String subtitle;
    private final String image;
    private final String accuracy;
    private final String impacts;
    private final String note;
    private final String analyze;
    private static final String paragraphSeparator = "\n\n";
    private String id;

    public FacebookPost(String title, String subtitle, String image,
                        String accuracy, String impacts, String note, String analyze) {
        this.title = Objects.requireNonNull(title);
        this.subtitle = Objects.requireNonNull(subtitle);
        this.image = Objects.requireNonNull(image);
        this.accuracy = Objects.requireNonNull(accuracy);
        this.impacts = Objects.requireNonNull(impacts);
        this.note = Objects.requireNonNull(note);
        this.analyze = Objects.requireNonNull(analyze);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(title).append(paragraphSeparator)
                .append(subtitle).append(paragraphSeparator);
        if (!impacts.isEmpty()) sb.append(impacts).append(paragraphSeparator);
        if (!note.isEmpty()) sb.append(note).append(paragraphSeparator);
        if (!analyze.isEmpty()) sb.append(analyze).append(accuracy);
        return sb
                .toString()
                .trim();
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

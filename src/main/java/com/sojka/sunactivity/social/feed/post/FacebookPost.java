package com.sojka.sunactivity.social.feed.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class FacebookPost extends SocialMediaPost {

    private static final String paragraphSeparator = "\n\n";

    private String title;
    private String subtitle;
    private String accuracy;
    private String impacts;
    private String note;
    private String analyze;

    public FacebookPost(String title, String subtitle, String image,
                        String accuracy, String impacts, String note, String analyze) {
        super(Objects.requireNonNull(image));
        this.title = Objects.requireNonNull(title);
        this.subtitle = Objects.requireNonNull(subtitle);
        this.accuracy = Objects.requireNonNull(accuracy);
        this.impacts = Objects.requireNonNull(impacts);
        this.note = Objects.requireNonNull(note);
        this.analyze = Objects.requireNonNull(analyze);
    }

    @Override
    public String getContent() {
        StringBuilder sb = new StringBuilder(title).append(paragraphSeparator)
                .append(subtitle).append(paragraphSeparator);
        if (!impacts.isEmpty()) sb.append(impacts).append(paragraphSeparator);
        if (!note.isEmpty()) sb.append(note).append(paragraphSeparator);
        if (!analyze.isEmpty()) sb.append(analyze).append(accuracy);

        return sb.toString().trim();
    }
}

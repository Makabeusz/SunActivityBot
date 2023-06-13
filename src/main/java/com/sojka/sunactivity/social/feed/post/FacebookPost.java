package com.sojka.sunactivity.social.feed.post;

public class FacebookPost implements SocialMediaPost {

    private final String title;
    private final String subtitle;
    private final String image;
    private final String accuracy;
    private final String impacts;
    private final String note;
    private final String analyze;

    public FacebookPost(String title, String subtitle, String image,
                        String accuracy, String impacts, String note, String analyze) {
        this.title = title;
        this.subtitle = subtitle;
        this.image = image;
        this.accuracy = accuracy;
        this.impacts = impacts;
        this.note = note;
        this.analyze = analyze;
    }

    @Override
    public String getContent() {
        return title
                .concat(subtitle)
                .concat(accuracy)
                .concat(impacts)
                .concat(note)
                .concat(analyze)
                .trim();
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return title + subtitle + accuracy + impacts + note + analyze;
    }

}

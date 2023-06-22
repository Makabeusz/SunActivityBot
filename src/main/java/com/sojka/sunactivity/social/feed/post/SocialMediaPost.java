package com.sojka.sunactivity.social.feed.post;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class SocialMediaPost {

    private String id;
    private String image;

    public SocialMediaPost(String image) {
        this.image = image;
    }

    public SocialMediaPost() {
    }

    abstract public String getContent();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
}

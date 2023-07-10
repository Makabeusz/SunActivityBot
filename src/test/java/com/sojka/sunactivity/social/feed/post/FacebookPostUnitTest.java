package com.sojka.sunactivity.social.feed.post;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FacebookPostUnitTest {

    SocialMediaPost dummyPost = new FacebookPost("title", "subtitle", "image",
            "Accuracy", "impacts", "note", "analyze"
    );

    @Test
    void getContent_dummyPost_paragraphsOnCorrectPlaces() {
        assertThat(dummyPost.getContent()).isEqualTo(
                """
                        title

                        subtitle
                                                
                        impacts
                                                
                        note
                                                
                        analyzeAccuracy"""
        );
    }
}
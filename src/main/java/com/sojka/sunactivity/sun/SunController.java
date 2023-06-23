package com.sojka.sunactivity.sun;

import com.sojka.sunactivity.social.feed.post.SocialMediaPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(path = "/sun")
@RequiredArgsConstructor
public class SunController {

    private final SunService service;

    @GetMapping(path = "/job")
    public ResponseEntity<Set<Set<SocialMediaPost>>> performDailyCmeCheckoutAndPosting() {
        return ResponseEntity.ok(service.getAndPostCmeData());
    }


}

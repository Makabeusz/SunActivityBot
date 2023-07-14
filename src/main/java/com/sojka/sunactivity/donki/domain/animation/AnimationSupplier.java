package com.sojka.sunactivity.donki.domain.animation;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.donki.http.DonkiHttpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Like producer in producer-consumer design pattern. The purpose of AnimationSupplier is to provide animation
 * for current {@link EarthGbCme} object in concurrent manner.
 */
@Slf4j
public class AnimationSupplier implements Runnable {

    private final AnimationQueue queue;
    private final DonkiHttpClient http;
    private final ConcurrentLinkedQueue<EarthGbCme> result;


    /**
     * @param queue  Queue preloaded with {@link EarthGbCme} without animations
     * @param http   Donki HTTP client
     * @param result empty holder for results
     */
    public AnimationSupplier(AnimationQueue queue,
                             DonkiHttpClient http,
                             ConcurrentLinkedQueue<EarthGbCme> result) {
        this.queue = queue;
        this.http = http;
        this.result = result;
    }

    @Override
    public void run() {
        while (true) {
            EarthGbCme cme;
            try {
                cme = queue.remove();
                obtainAnimations(cme);
                result.add(cme);
            } catch (NoSuchElementException e) {
                break;
            }
        }
    }

    // TODO: request for animations - better name also this should be moved to HTTP client
    private void obtainAnimations(EarthGbCme cme) {
        String html = http.getViewContent(cme.getSimulationUrl()).getBody();
        List<String> animationUrls = Objects.requireNonNull(html).lines()
                .filter(line -> line.contains("Inner Planets Link = <a href=\""))
                .filter(line -> line.contains("anim.tim-den.gif") || line.contains("anim.tim-vel.gif"))
                .map(line -> line
                        .replaceAll("^.*Inner Planets Link = <a href=\"", "")
                        .replaceAll("\">.*$", ""))
                .toList();
        for (String animation : animationUrls) {
            if (animation.endsWith("-den.gif")) {
                cme.setAnimationDensity(animation);
            } else if (animation.endsWith("-vel.gif")) {
                cme.setAnimationVelocity(animation);
            } else {
                log.error("Unrecognized animation URI: " + animation);
            }
        }
    }
}

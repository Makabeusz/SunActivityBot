package com.sojka.sunactivity.donki.domain.animation;

import com.sojka.sunactivity.donki.domain.EarthGbCme;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The wrapper for {@link LinkedBlockingQueue}. It exposes only two methods {@link AnimationQueue#remove()}
 * and {@link AnimationQueue#initialSize()}. Its purpose is to take {@link EarthGbCme} objects and with help
 * of {@link AnimationSupplier} load all its animations.<br>
 * Created on top of producer-consumer design pattern idea.
 */
public class AnimationQueue {

    private final BlockingQueue<EarthGbCme> queue;
    private final int size;

    public AnimationQueue(Collection<EarthGbCme> cmes) {
        this.size = cmes.size();
        if (size < 1) {
            throw new IllegalArgumentException("Must provide at least 1 element collection");
        }
        queue = new LinkedBlockingQueue<>(size);
        queue.addAll(cmes);
    }

    /**
     * Same as {@link BlockingQueue#remove()}, but explicitly force to catch {@link NoSuchElementException}
     */
    public EarthGbCme remove() throws NoSuchElementException {
        return queue.remove();
    }

    /**
     * @return Initial size of loaded collection
     */
    public int initialSize() {
        return size;
    }

}

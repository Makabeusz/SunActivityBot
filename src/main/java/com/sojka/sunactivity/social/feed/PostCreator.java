package com.sojka.sunactivity.social.feed;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.FacebookPost;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostCreator {

    private static final String TITLE_BASE = "%s-type coronal mass ejection %s\n\n";
    private static final String SUBTITLE_BASE = """
            NASA sun observatories detected coronal mass ejection started at %s%s.
            According to the simulations it will deliver glancing blow to the Earth at %s reaching the \
            speed of %d km/s.%s
            """;
    private static final String DURATION_END_TIME_BASE = " The CME will be affecting earth up to %s.";
    private static final String ACCURACY_BASE = "The analyze is %s\n\n";
    private static final String NASA_NOTE_BASE = "NASA scientist description:\n%s\n\n";

    public static synchronized SocialMediaPost createFacebookPost(EarthGbCme cme) {
        Objects.requireNonNull(cme);
        String image = cme.getAnimationDensity() == null ? "": cme.getAnimationDensity();
        return new FacebookPost(createTitle(cme), createSubtitle(cme), image, createAccuracyHeading(cme),
                createImpactsHeading(cme), createNoteHeading(cme), createAnalyzeHeading(cme));
    }

    static String createTitle(EarthGbCme cme) {
        String level;
        switch (cme.getAnalyze().getScore()) {
            case S, C -> level = "information";
            case O -> level = "alert";
            case R -> level = "red alert!";
            case ER -> level = "danger!";
            default -> throw new RuntimeException("Unrecognizable SCORE");
        }
        return String.format(TITLE_BASE, cme.getAnalyze().getScore(), level);
    }

    static String createSubtitle(EarthGbCme cme) {
        EarthGbCme.Time time = cme.getTime();
        String region = "";
        if (cme.getActiveRegion() != null) region = " in active region " + cme.getActiveRegion();
        return String.format(SUBTITLE_BASE,
                time.getStartTime(),
                region,
                time.getArrivalTime(),
                cme.getAnalyze().getSpeed().intValue(),
                createDurationEndTimeHeading(cme));
    }

    static String createDurationEndTimeHeading(EarthGbCme cme) {
        if (cme.getTime().getDuration() == null) return "";

        float duration = cme.getTime().getDuration();
        int minutes = (int) ((duration * 60) % 60);
        int hours = (int) duration;
        return String.format(DURATION_END_TIME_BASE,
                ZonedDateTime.parse(cme.getTime().getArrivalTime())
                .plusHours(hours)
                .plusMinutes(minutes));
    }

    static String createAccuracyHeading(EarthGbCme cme) {
        Boolean accuracy = cme.getAnalyze().getIsMostAccurate();
        return String.format(ACCURACY_BASE, accuracy == null || !accuracy
                ? "not most accurate."
                : "most accurate!");
    }

    static String createImpactsHeading(EarthGbCme cme) {
        List<EarthGbCme.Impact> impacts = cme.getImpacts();
        if (impacts == null || impacts.isEmpty()) {
            return "";
        }

        LinkedList<EarthGbCme.Impact> marsFirstSorted = impacts.stream()
                .sorted((i1, i2) -> {
                    if (i1.getLocation().equalsIgnoreCase("Mars")) return -1;
                    return ZonedDateTime.parse(i1.getArrivalTime())
                            .compareTo(ZonedDateTime.parse(i2.getArrivalTime()));
                })
                .collect(Collectors.toCollection(LinkedList::new));

        boolean isMars = marsFirstSorted.getFirst().getLocation().equalsIgnoreCase("Mars");
        boolean plural;
        if (isMars) {
            plural = marsFirstSorted.size() > 2;
        } else {
            plural = marsFirstSorted.size() > 1;
        }

        StringBuilder sb = new StringBuilder("Ejected sun particles will reach ");

        if (isMars) {
            EarthGbCme.Impact mars = marsFirstSorted.removeFirst();
            sb.append("Mars at ").append(ZonedDateTime.parse(mars.getArrivalTime()));
            if (mars.getIsGlancingBlow()) {
                sb.append(" delivering glancing blow to the planet!\n");
            } else {
                sb.append(".\n");
            }
            if (marsFirstSorted.size() > 1) {
                sb.append("Other ");
            } else {
                return sb
                        .append("\n")
                        .toString();
            }
        }

        sb.append("NASA instrument");
        if (plural) sb.append("s");
        if (isMars) sb.append(" hit by sun particles");
        sb.append(":\n");

        for (EarthGbCme.Impact impact : marsFirstSorted) {
            sb.append("- ")
                    .append(impact.getLocation())
                    .append(" at ")
                    .append(impact.getArrivalTime());
            if (impact.getIsGlancingBlow()) {
                sb.append(" delivering glancing blow to the instrument!");
            }
            sb.append("\n");
        }

        return sb
                .append("\n")
                .toString();
    }

    static String createNoteHeading(EarthGbCme cme) {
        if (cme.getNote() == null || cme.getNote().isBlank()) {
            return "";
        }
        return String.format(NASA_NOTE_BASE, cme.getNote());
    }

    static String createAnalyzeHeading(EarthGbCme cme) {
        StringBuilder sb = new StringBuilder();
        sb.append("Analyze:\n");

        int initialSize = sb.length();

        if (cme.getAnalyze() != null) {
            Optional.ofNullable(cme.getAnalyze().getLatitude())
                    .ifPresent(latitude -> sb
                            .append("Latitude: ")
                            .append(latitude)
                            .append("\n"));
            Optional.ofNullable(cme.getAnalyze().getLongitude())
                    .ifPresent(longitude -> sb
                            .append("Longitude: ")
                            .append(longitude)
                            .append("\n"));
            Optional.ofNullable(cme.getAnalyze().getHalfAngle())
                    .ifPresent(angle -> sb
                            .append("Half-angle: ")
                            .append(angle)
                            .append("\n"));
        }
        if (cme.getKpIndex() != null) {
            Optional.ofNullable(cme.getKpIndex().getKp18())
                    .ifPresent(index -> sb
                            .append("KP index 18°: ")
                            .append(index)
                            .append("\n"));
            Optional.ofNullable(cme.getKpIndex().getKp90())
                    .ifPresent(index -> sb
                            .append("KP index 90°: ")
                            .append(index)
                            .append("\n"));
            Optional.ofNullable(cme.getKpIndex().getKp135())
                    .ifPresent(index -> sb
                            .append("KP index 135°: ")
                            .append(index)
                            .append("\n"));
            Optional.ofNullable(cme.getKpIndex().getKp180())
                    .ifPresent(index -> sb
                            .append("KP index 180°: ")
                            .append(index)
                            .append("\n"));
        }
        if (sb.length() == initialSize) {
            return "";
        }
        return sb.toString();
    }
}

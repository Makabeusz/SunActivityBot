package com.sojka.sunactivity.social.feed;

import com.sojka.sunactivity.donki.domain.EarthGbCme;
import com.sojka.sunactivity.social.feed.post.FacebookPost;
import com.sojka.sunactivity.social.feed.post.SocialMediaPost;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostCreator {

    private static final String TITLE_BASE = "%s-type coronal mass ejection %s\n\n";
    private static final String SUBTITLE_BASE = """
            NASA sun observatories detected coronal mass ejection started at %s%s.
            According to the simulations it will deliver glancing blow to the Earth at %s reaching the \
            speed of %d km/s.%s
            """;

    private static final String ACCURACY = "The analyze is %s\n\n";

    public static SocialMediaPost createFacebookPost(EarthGbCme cme) {
        return new FacebookPost(createTitle(cme), createSubtitle(cme), cme.getAnimationDensity(), createAccuracy(cme),
                createImpacts(cme), createNote(cme), createAnalyze(cme));
    }

    static String createAccuracy(EarthGbCme cme) {
        return String.format(ACCURACY, cme.getAnalyze().getIsMostAccurate()
                ? "most accurate!"
                : "not most accurate.");
    }

    static String createImpacts(EarthGbCme cme) {
        List<EarthGbCme.Impact> impacts = cme.getImpacts();
        if (impacts.isEmpty()) {
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
        boolean plural = isMars && marsFirstSorted.size() > 2;

        StringBuilder sb = new StringBuilder("Ejected sun particles will reach ");

        if (isMars) {
            EarthGbCme.Impact mars = marsFirstSorted.removeFirst();
            sb.append("Mars at ").append(ZonedDateTime.parse(mars.getArrivalTime()));
            if (mars.getIsGlancingBlow()) {
                sb.append(" delivering glancing blow to the planet!\n");
            } else {
                sb.append(".\n");
            }
        }

        sb.append("Other NASA instrument");
        if (plural) sb.append("s");
        sb.append(" hit by sun particles:\n");

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

    static String createNote(EarthGbCme cme) {
        return "NASA scientist description:\n" + cme.getNote() + "\n\n";
    }

    static String createAnalyze(EarthGbCme cme) {
        StringBuilder sb = new StringBuilder();
        sb.append("Analyze:\n");

        int initialSize = sb.length();

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
                        .append("Source location: ")
                        .append(angle)
                        .append("\n"));
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

        if (sb.length() == initialSize) {
            return "";
        }
        return sb.toString();
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
                " The CME will be affecting earth up to " + calculateDurationEndTime(cme) + ".");
    }

    static String calculateDurationEndTime(EarthGbCme cme) {
        if (cme.getTime().getDuration() == null) return "";

        float duration = cme.getTime().getDuration();
        int minutes = (int) ((duration * 60) % 60);
        int hours = (int) duration;
        return ZonedDateTime.parse(cme.getTime().getArrivalTime())
                .plusHours(hours)
                .plusMinutes(minutes)
                .toString();
    }
}

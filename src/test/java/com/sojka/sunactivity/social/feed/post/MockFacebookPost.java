package com.sojka.sunactivity.social.feed.post;

public final class MockFacebookPost {

    private MockFacebookPost() {
        throw new AssertionError();
    }

    public static FacebookPost firstMinimal() {
        return new FacebookPost("O-type coronal mass ejection alert", """
                NASA sun observatories detected coronal mass ejection started at 2023-04-20T06:12Z.
                According to the simulations it will deliver glancing blow to the Earth at 2023-04-25T01:28Z \
                reaching the speed of 1758 km/s.""",
                "", "The analyze is most accurate!", "", "", "");
    }

    public static FacebookPost rich() {
        return new FacebookPost("O-type coronal mass ejection alert", """
                NASA sun observatories detected coronal mass ejection started at 2023-04-18T23:48Z \
                in active region 13283.
                According to the simulations it will deliver glancing blow to the Earth at 2023-04-23T19:25Z \
                reaching the speed of 1087 km/s. The CME will be affecting earth up to 2023-04-24T22:31Z.""",
                "http://iswa.gsfc.nasa.gov/downloads/20230505_111000_2.0_anim.tim-den.gif",
                "The analyze is not most accurate.", """
                Ejected sun particles will reach Mars at 2023-05-20T06:00Z delivering glancing blow to the planet!
                Other NASA instruments hit by sun particles:
                - STEREO A at 2023-04-23T18:24Z
                - OSIRIS-REx at 2023-04-24T22:44Z""",
                "Faint and narrow CME seen to the W in SOHO LASCO C2/C3. Data gap in STEREO-A COR2.", """
                Analyze:
                Bulk measurement following a brighter leading edge seen within the core structure of the halo CME in \
                later frames mostly to the south and east.
                Latitude: -14.0
                Longitude: 14.0
                Half-angle: 45.0
                KP index 90°: 6
                KP index 135°: 7
                KP index 180°: 8
                """);
    }

    public static FacebookPost secondMinimal() {
        return new FacebookPost("R-type coronal mass ejection red alert!", """
                NASA sun observatories detected coronal mass ejection started at 2023-04-20T06:13Z.
                According to the simulations it will deliver glancing blow to the Earth at 2023-04-25T01:36Z \
                reaching the speed of 2543 km/s.""",
                "", "The analyze is not most accurate.", "", "", "");
    }

    public static FacebookPost thirdMinimal() {
        return new FacebookPost("S-type coronal mass ejection information", """
                NASA sun observatories detected coronal mass ejection started at 2023-04-20T07:09Z.
                According to the simulations it will deliver glancing blow to the Earth at 2023-04-25T02:12Z \
                reaching the speed of 445 km/s.""",
                "", "The analyze is most accurate!", "", "", "");
    }

    public static String richEarthGbCmeString() {
        return """
                O-type coronal mass ejection alert

                NASA sun observatories detected coronal mass ejection started at 2023-04-18T23:48Z in active region 13283.
                According to the simulations it will deliver glancing blow to the Earth at 2023-04-23T19:25Z reaching the speed of 1087 km/s. The CME will be affecting earth up to 2023-04-24T22:31Z.

                                
                Faint and narrow CME seen to the W in SOHO LASCO C2/C3. Data gap in STEREO-A COR2.

                Analyze:
                Bulk measurement following a brighter leading edge seen within the core structure of the halo CME in later frames mostly to the south and east.
                Latitude: -14.0
                Longitude: 14.0
                Half-angle: 45.0
                KP index 90°: 6
                KP index 135°: 7
                KP index 180°: 8
                """;
    }

}
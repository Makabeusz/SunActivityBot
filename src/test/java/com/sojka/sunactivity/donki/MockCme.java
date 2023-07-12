package com.sojka.sunactivity.donki;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sojka.sunactivity.donki.domain.Cme;
import com.sojka.sunactivity.donki.domain.WsaEnlil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MockCme {

    private static final Cme richCme;

    private MockCme() {
        throw new AssertionError();
    }

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            richCme = mapper.readValue(getRichCmeString(), Cme.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Cme getRichCme() {
        return richCme;
    }

    public static List<Cme> getNineCmes() {
        List<Cme> cmes = new ArrayList<>();
        var basicAnalyze = Cme.CmeAnalyze.builder()
                .enlilList(List.of(WsaEnlil.builder()
                        .isEarthGB(true)
                        .link("https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24725/-1")
                        .build()))
                .type("S")
                .build();
        for (int i = 0; i < 9; i++) {
            cmes.add(Cme.builder()
                    .activityID(String.format("2023-07-12T12:00:00-CME-00%d", i))
                    .startTime(String.format("2023-07-12T12:0%dZ", i))
                    .cmeAnalyses(List.of(basicAnalyze))
                    .build());
        }
        return cmes;
    }

    public static String getRichCmeString() {
        return """
                {
                  "activityID": "2023-04-18T23:48:00-CME-001",
                  "catalog": "M2M_CATALOG",
                  "startTime": "2023-04-18T23:48Z",
                  "sourceLocation": "S20E90",
                  "activeRegionNum": 13283,
                  "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CME/24696/-1",
                  "note": "Faint and narrow CME seen to the W in SOHO LASCO C2/C3. Data gap in STEREO-A COR2.",
                  "instruments": [
                    {
                      "displayName": "SOHO: LASCO/C2"
                    },
                    {
                      "displayName": "SOHO: LASCO/C3"
                    }
                  ],
                  "cmeAnalyses": [
                    {
                      "time21_5": "2023-04-19T09:40Z",
                      "latitude": 1.0,
                      "longitude": 90.0,
                      "halfAngle": 12.0,
                      "speed": 371.0,
                      "type": "S",
                      "isMostAccurate": true,
                      "note": "Leading edge is diffuse. CME is visible for one frame in STEREO A COR2.",
                      "levelOfData": 0,
                      "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CMEAnalysis/24697/-1",
                      "enlilList": [
                        {
                          "modelCompletionTime": "2023-04-19T17:46Z",
                          "au": 2.0,
                          "estimatedShockArrivalTime": null,
                          "estimatedDuration": null,
                          "rmin_re": null,
                          "kp_18": null,
                          "kp_90": null,
                          "kp_135": null,
                          "kp_180": null,
                          "isEarthGB": false,
                          "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24698/-1",
                          "impactList": [
                            {
                              "isGlancingBlow": false,
                              "location": "Parker Solar Probe",
                              "arrivalTime": "2023-04-21T08:00Z"
                            },
                            {
                              "isGlancingBlow": true,
                              "location": "Solar Orbiter",
                              "arrivalTime": "2023-04-20T06:00Z"
                            }
                          ],
                          "cmeIDs": [
                            "2023-04-18T23:48:00-CME-001"
                          ]
                        },
                        {
                          "modelCompletionTime": "2023-04-21T13:12Z",
                          "au": 2.0,
                          "estimatedShockArrivalTime": "2023-04-23T19:05Z",
                          "estimatedDuration": 25.1,
                          "rmin_re": 5.1,
                          "kp_18": 5,
                          "kp_90": 6,
                          "kp_135": 7,
                          "kp_180": 8,
                          "isEarthGB": true,
                          "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24715/-1",
                          "impactList": [
                            {
                              "isGlancingBlow": false,
                              "location": "Parker Solar Probe",
                              "arrivalTime": "2023-04-23T04:24Z"
                            },
                            {
                              "isGlancingBlow": false,
                              "location": "Solar Orbiter",
                              "arrivalTime": "2023-04-22T06:53Z"
                            }
                          ],
                          "cmeIDs": [
                            "2023-04-21T06:24:00-CME-001"
                          ]
                        },
                        {
                          "modelCompletionTime": "2023-04-21T20:31Z",
                          "au": 2.0,
                          "estimatedShockArrivalTime": "2023-04-23T19:25Z",
                          "estimatedDuration": 27.1,
                          "rmin_re": 5.1,
                          "kp_18": null,
                          "kp_90": 6,
                          "kp_135": 7,
                          "kp_180": 8,
                          "isEarthGB": true,
                          "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24725/-1",
                          "impactList": [
                            {
                              "isGlancingBlow": false,
                              "location": "OSIRIS-REx",
                              "arrivalTime": "2023-04-24T22:44Z"
                            },
                            {
                              "isGlancingBlow": true,
                              "location": "STEREO A",
                              "arrivalTime": "2023-04-23T18:24Z"
                            },
                            {
                              "isGlancingBlow": true,
                              "location": "Mars",
                              "arrivalTime": "2023-05-20T06:00Z"
                            }
                          ],
                          "cmeIDs": [
                            "2023-04-21T18:12:00-CME-001"
                          ]
                        }
                      ]
                    },
                    {
                      "time21_5": "2023-04-21T21:09Z",
                      "latitude": -13.0,
                      "longitude": 19.0,
                      "halfAngle": 43.0,
                      "speed": 1204.0,
                      "type": "O",
                      "isMostAccurate": true,
                      "note": "Measurement of the bulk portion, based on the best fit between preliminary imagery of SOHO LASCO C2 and STEREO A COR2.",
                      "levelOfData": 0,
                      "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CMEAnalysis/24724/-1",
                      "enlilList": [
                        {
                          "modelCompletionTime": "2023-04-21T20:10Z",
                          "au": 2.0,
                          "estimatedShockArrivalTime": "2023-04-23T21:35Z",
                          "estimatedDuration": 22.1,
                          "rmin_re": 6.1,
                          "kp_18": null,
                          "kp_90": 4,
                          "kp_135": 6,
                          "kp_180": 7,
                          "isEarthGB": false,
                          "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/WSA-ENLIL/24725/-1",
                          "impactList": [
                            {
                              "isGlancingBlow": false,
                              "location": "OSIRIS-REx",
                              "arrivalTime": "2023-04-24T22:44Z"
                            },
                            {
                              "isGlancingBlow": false,
                              "location": "STEREO A",
                              "arrivalTime": "2023-04-23T18:24Z"
                            }
                          ],
                          "cmeIDs": [
                            "2023-04-21T18:12:00-CME-001"
                          ]
                        }
                      ]
                    },
                    {
                      "time21_5": "2023-04-21T20:38Z",
                      "latitude": -22.0,
                      "longitude": -2.0,
                      "halfAngle": 55.0,
                      "speed": 1432.0,
                      "type": "O",
                      "isMostAccurate": false,
                      "note": "Shock front measurement assuming a larger half-width (>45 degrees) given the signature seen in SDO 193 and the relatively flattened shape the faint leading edge of the halo takes at its poles to the north and south.",
                      "levelOfData": 0,
                      "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CMEAnalysis/24721/-1",
                      "enlilList": null
                    },
                    {
                      "time21_5": "2023-04-21T21:29Z",
                      "latitude": -14.0,
                      "longitude": 14.0,
                      "halfAngle": 45.0,
                      "speed": 1087.0,
                      "type": "O",
                      "isMostAccurate": false,
                      "note": "Bulk measurement following a brighter leading edge seen within the core structure of the halo CME in later frames mostly to the south and east.",
                      "levelOfData": 0,
                      "link": "https://webtools.ccmc.gsfc.nasa.gov/DONKI/view/CMEAnalysis/24722/-1",
                      "enlilList": null
                    }
                  ],
                  "linkedEvents": [
                    {
                      "activityID": "2023-04-21T17:44:00-FLR-001"
                    },
                    {
                      "activityID": "2023-04-23T11:48:00-SEP-001"
                    },
                    {
                      "activityID": "2023-04-23T13:58:00-SEP-001"
                    },
                    {
                      "activityID": "2023-04-23T14:00:00-IPS-001"
                    }
                  ]
                }
                """;
    }

    public static String getHtmlWithAnimations() {
        return """
                <html>
                            
                <head>
                    <link rel="stylesheet" type="text/css" media="all" href="/DONKI/resources/css/styles.css">
                    <title>
                        View Space Weather Activity
                    </title>
                </head>
                <body>
                <table width="100%" border="1" cellpadding="2" cellspacing="2" align="left">
                    <tr>
                        <td colspan="2" align="left">
                            <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
                            <html>
                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                                <meta name="description" content="CCMC DONKI Application">
                                <meta name="orgcode" content="Code674">
                                <meta name="rno" content="Dr. Masha Kuznetsova">
                                <meta name="content-owner" content="Dr. Masha Kuznetsova">
                                <meta name="webmaster" content="Chiu Wiegand">
                                <title>DONKI Header</title>
                                <link rel="shortcut icon" href="https://webtools.ccmc.gsfc.nasa.gov/favicon.ico"
                                      type="image/x-icon">
                                <link rel="icon" href="https://webtools.ccmc.gsfc.nasa.gov/favicon.ico" type="image/x-icon">
                                <!-- Google tag UA retired by July 2023 -->
                                <script>
                \t\t\t\t\t\t(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
                  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
                  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
                  ga('create', 'UA-43747106-1', 'auto');
                  ga('send', 'pageview');
                \t\t\t\t\t</script>
                                <!-- Agency Google tag -->
                                <script language="javascript" id="_fed_an_ua_tag"
                                        src="https://dap.digitalgov.gov/Universal-Federated-Analytics-Min.js?agency=NASA&subagency=GSFC&yt=true&dclink=true">
                                </script>
                                <!-- Google G4 tag (gtag.js) -->
                                <script async src="https://www.googletagmanager.com/gtag/js?id=G-CW9J2XG88Y"></script>
                                <script>
                \t\t\t\t\t\twindow.dataLayer = window.dataLayer || [];
                  function gtag(){dataLayer.push(arguments);}
                  gtag('js', new Date());
                  gtag('config', 'G-CW9J2XG88Y');
                \t\t\t\t\t</script>
                            </head>
                            <body>
                            <H1>
                                <a href="https://ccmc.gsfc.nasa.gov"
                                   target="_blank"><img src="/DONKI/resources/images/ccmcLogo.gif"/></a>
                                Space Weather Database Of Notifications, Knowledge, Information (DONKI)
                            </H1>
                            </body>
                            </html>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
                            <html>
                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                                <title>DONKI: Menu Page</title>
                            </head>
                            <body>
                            <h4>Go to:</h4>
                            <ul>
                                <li><a href="https://ccmc.gsfc.nasa.gov/tools/DONKI/" target="_blank"> About DONKI</a></li>
                                <li><a href="/DONKI/"> DONKI Home</a></li>
                                <li><a href="/DONKI/search/"> Search Space Weather Activity</a></li>
                                <li><a href="/DONKI/search/alerts"> Search Notification Archive</a></li>
                                <!-- sec:authorize access="hasIpAddress('169.154.198.0/24') or hasIpAddress('128.183.0.0/16') or hasIpAddress('198.120.0.0/16') or hasIpAddress('128.154.0.0/16') or hasIpAddress('156.68.0.0/16') or hasIpAddress('10.250.0.0/16')"-->
                                <li><a href="/DONKI/login"> Login</a></li>
                            </ul>
                            </body>
                            </html>
                        </td>
                        <td width="80%">
                            <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

                            <html>
                            <script>
                \t\t\t\t\tfunction confirmDeleteLink() {
                \t\tvar confirmMessage = "Are you sure you want to delete the link activity?";

                \t\tif (confirm(confirmMessage) == false) {
                \t\t\treturn false;
                \t\t} else {
                \t\t\treturn true;
                \t\t}
                \t}
                \t\t\t\t</script>
                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                                <title>DONKI: View SW Activity</title>
                            </head>
                            <body>
                            <table width="100%">
                                <tr>
                                    <td>
                                        <b>WSA-ENLIL+Cone Model For 2.0 AU with Completion Time:
                                            2023-06-01T12:56Z</b>
                                        <br>
                                        <br>
                                        <b>Model Inputs:</b>
                                        <br>

                                        <a href=" /DONKI/view/CME/25433/1 ">2023-06-01T00:36:00-CME-001</a>
                                        with <a href=" /DONKI/view/CMEAnalysis/25434/1 ">CME
                                        Analysis</a>:
                                        Lon.=80.0, Lat.=54.0, Speed=1662.0, HalfAngle=38.0, Time21.5=2023-06-01T02:44Z <br>

                                        <br>
                                        <b>Model Outputs:</b>
                                        <br>
                                        Earth Impact:<br>
                                        No or little impact to Earth.<br>
                                        <br>
                                        Other Location(s) Impact:<br>
                                        Parker Solar Probe with estimated shock arrival time 2023-06-02T07:17Z
                                        <br>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den.gif</a>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel.gif</a>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den-Stereo_A.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den-Stereo_A.gif</a>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den-Stereo_B.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-den-Stereo_B.gif</a>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel-Stereo_A.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel-Stereo_A.gif</a>
                                        <br>
                                        Inner Planets Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel-Stereo_B.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_anim.tim-vel-Stereo_B.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Kp_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Kp_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_PSP_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_PSP_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_STA_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_STA_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_STB_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_STB_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Mars_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Mars_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Merc_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Merc_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_SolO_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_SolO_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Venus_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Venus_timeline.gif</a>
                                        <br>
                                        Timelines Link = <a href="http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Osiris_timeline.gif">http://iswa.gsfc.nasa.gov/downloads/20230601_024400_2.0_ENLIL_CONE_Osiris_timeline.gif</a>
                                        <br>
                                        <br>Note
                                        :<br>
                                        <br>
                                        <br>
                                        <I>Submitted on 2023-06-01T13:04Z by Mattie
                                            Anastopulos </I>
                                        <br>
                                        <br>
                                        A Notification with ID <a href=" /DONKI/view/Alert/25436/1 ">
                                        20230601-AL-001</a> was sent on 2023-06-01T13:07Z<br>
                                    </td>
                                </tr>
                                <tr>
                                    <td><br><br></td>
                                </tr>
                                <!-- Display all comments related to this activity -->
                                <tr>
                                    <td>Comments by users:</td>
                                </tr>
                                <tr>
                                    <td>0 comment received.</td>
                                </tr>
                            </table>
                            </body>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
                            <html>
                            <head>
                                <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
                                <title>Insert title here</title>
                            </head>
                            <body>
                            <p>
                                <B>The Moon to Mars Space Weather Analysis Office and other entities provide data to DONKI as a courtesy to the community.</B><br>
                                <B>By visiting this site, you acknowledge that you understand and agree to the <a href="/DONKI/Disclaimer">Important Disclaimer Notice</a></B>
                                <br><br>
                                <B>
                                    If you are looking for the official U.S. Government forecast for space weather, please go to NOAA's Space Weather Prediction Center (<a href="https://swpc.noaa.gov" target="_blank">https://swpc.noaa.gov</a>). This "Experimental Research Information" consists of preliminary NASA research products and should be interpreted and used accordingly.
                                </B>
                            </p>
                            <p style="padding-bottom: 0pt; padding-top: 0pt;text-align: center ">
                                Curator: <a title="mailto: chiu.wiegand@nasa.gov?" href="mailto:chiu.wiegand@nasa.gov ">Chiu
                                Wiegand</a> |
                                NASA Official: <a title="mailto:Maria.M.Kuznetsova@nasa.gov?subject=DONKI"
                                                  href="mailto:Maria.M.Kuznetsova@nasa.gov?subject=DONKI">Dr. Masha Kuznetsova</a> |
                                <a onclick="window.open(this.href); return false;"
                                   title="https://www.nasa.gov/about/highlights/HP_Privacy.html"
                                   href="https://www.nasa.gov/about/highlights/HP_Privacy.html" onkeypress="window.open(this.href); return
                false;">Privacy and Security Notices</a> |
                                <a onclick="window.open(this.href); return false;"
                                   title="https://ccmc.gsfc.nasa.gov/data-consent-agreement/"
                                   href="https://ccmc.gsfc.nasa.gov/data-consent-agreement/" onkeypress="window.open(this.href); return
                false;">CCMC Data Collection Consent Agreement</a>
                            </p>
                            </body>
                            </html>
                        </td>
                    </tr>
                </table>
                </body>
                </html>""";
    }
}

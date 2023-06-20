package com.sojka.sunactivity.donki.controller;

import com.sojka.sunactivity.donki.DonkiService;
import com.sojka.sunactivity.donki.dto.EarthGbCmeDto;
import com.sojka.sunactivity.donki.mapper.DonkiDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/donki")
@RequiredArgsConstructor
public class DonkiController {

    private final DonkiService donki;

    @GetMapping(path = "/cme")
    public Set<EarthGbCmeDto> getEarthCme(
            @RequestParam(name = "from", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().minusDays(30L)}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(name = "to", required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return donki.getEarthGbCmes(from, to).stream()
                .map(DonkiDtoMapper::toEarthGbCmeDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping(path = "/cme/yesterday")
    public Set<EarthGbCmeDto> getYesterdayEarthCme() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return donki.getEarthGbCmes(yesterday, yesterday).stream()
                .map(DonkiDtoMapper::toEarthGbCmeDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping(path = "/db/cme")
    public List<EarthGbCmeDto> getEarthCmeFromDb(
            @RequestParam(name = "from", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().minusDays(30L)}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam(name = "to", required = false, defaultValue = "#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return donki.getSavedEarthGbCme(from, to).stream()
                .map(DonkiDtoMapper::toEarthGbCmeDto)
                .toList();
    }

    @GetMapping(path = "/db/cme/yesterday")
    public List<EarthGbCmeDto> getYesterdayEarthCmeFromDb() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return donki.getSavedEarthGbCme(yesterday, yesterday).stream()
                .map(DonkiDtoMapper::toEarthGbCmeDto)
                .toList();
    }

}

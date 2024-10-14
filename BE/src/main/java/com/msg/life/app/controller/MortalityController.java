package com.msg.life.app.controller;

import com.msg.life.app.service.MortalidadeService;
import com.msg.life.app.entity.Mortality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller that gets info about Mortality Rates
 */
@RestController
@RequestMapping("/mortality")
public class MortalityController {

    @Autowired
    private MortalidadeService service;

    /**
     * Gets all mortality rate years
     * @return list of year
     */
    @GetMapping("/")
    public List<Integer> getAvailableYears() {
        return service.getAvailableYears();
    }

    /**
     * Get mortality rates by year
     * @param year
     * @return
     */
    @GetMapping("/{year}")
    public List<Mortality> getMortalityByYear(@PathVariable int year) {
        return service.getMortalityByYear(year);
    }

    /**
     * Saves Mortality rate
     * @param mortality data to sabe
     * @return updated mortality rate
     */
    @PostMapping("/")
    public Mortality createOrUpdateMortality(@RequestBody Mortality mortality) {
        return service.saveOrUpdateMortality(mortality);
    }

    /**
     * upload file with mortality rates and replaces
     * @param file csv file
     * @param year year you want to update/replace
     * @throws IOException
     */
    @PostMapping("/upload/{year}")
    public void uploadCsv(@RequestParam("file") MultipartFile file, @PathVariable int year) throws IOException {
        service.saveCsvData(file, year);
    }
}

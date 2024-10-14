package com.msg.life.app.service;

import com.msg.life.app.entity.Mortality;
import com.msg.life.app.repository.MortalityRepository;
import com.msg.life.app.PopulationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStreamReader;
import java.util.ArrayList;

@Service
public class MortalidadeService {

    @Autowired
    private MortalityRepository repository;


    @Autowired
    private PopulationService populationService;

    /**
     * Gets all mortality rate years available
     * @return list of year
     */
    public List<Integer> getAvailableYears() {
        return repository.findAllYearInterval();
    }

    /**
     * Get mortality rates by year
     *
     * @param year
     * @return
     */
    public List<Mortality> getMortalityByYear(int year) {
        return repository.findByYearInterval(year);
    }

    /**
     * Saves Mortality rate
     *
     * @param mortality data to sabe
     * @return updated mortality rate
     */
    public Mortality saveOrUpdateMortality(Mortality mortality) {
        Mortality existing = repository.findByCountryAndYearInterval(mortality.getCountry(), mortality.getYearInterval());
        if (existing != null) {
            existing.setTxFemale(mortality.getTxFemale());
            existing.setTxMale(mortality.getTxMale());
            existing.setFemalePopulation(populationService.getFemalePopulation(mortality.getCountry(), mortality.getYearInterval()));
            existing.setMalePopulation(populationService.getMalePopulation(mortality.getCountry(), mortality.getYearInterval()));
            return repository.save(existing);
        } else {
            return repository.save(mortality);
        }
    }


    /**
     * upload file with mortality rates and replaces
     *
     * @param file csv file
     * @param year year you want to update/replace
     * @throws IOException
     */
    @Transactional
    public void saveCsvData(MultipartFile file, int year) throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        List<Mortality> mortalities = new ArrayList<>();

        for (CSVRecord csvRecord : csvParser) {
            Mortality mortality = new Mortality();
            mortality.setCountry(csvRecord.get("pais"));
            mortality.setTxFemale(BigDecimal.valueOf(Double.parseDouble(csvRecord.get("tx_female"))));
            mortality.setTxMale(BigDecimal.valueOf(Double.parseDouble(csvRecord.get("tx_male"))));
            mortality.setYearInterval(year);
            mortality.setMalePopulation(populationService.getMalePopulation(mortality.getCountry(), year));
            mortality.setFemalePopulation(populationService.getFemalePopulation(mortality.getCountry(), year));
            mortalities.add(mortality);
        }

        repository.deleteByYearInterval(year);
        repository.saveAll(mortalities);
    }
}
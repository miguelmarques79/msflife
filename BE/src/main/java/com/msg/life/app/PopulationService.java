package com.msg.life.app;

import org.springframework.stereotype.Service;

@Service
public class PopulationService {
    public long getFemalePopulation(String country, int year) {
// Mock implementation
        return 5145768L;
    }

    public long getMalePopulation(String country, int year) {
// Mock implementation
        return 4523172L;
    }
}

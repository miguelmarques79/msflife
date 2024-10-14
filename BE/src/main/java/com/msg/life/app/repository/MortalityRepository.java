package com.msg.life.app.repository;

import com.msg.life.app.entity.Mortality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MortalityRepository extends JpaRepository<Mortality, Long> {
    List<Mortality> findByYearInterval(int yearInterval);

    Mortality findByCountryAndYearInterval(String country, int yearInterval);

    void deleteByYearInterval(int yearInterval);

    @Query("SELECT DISTINCT mortality.yearInterval FROM Mortality mortality")
    List<Integer> findAllYearInterval();

}

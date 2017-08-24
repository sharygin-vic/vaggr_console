package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;
import java.util.List;

/**
 * Created by dell on 09-Jul-17.
 */
public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> getJavaVacancies(String vacancySearchString, String vacancyLocationName) {
        return strategy.getVacancies(vacancySearchString, vacancyLocationName);
    }
}

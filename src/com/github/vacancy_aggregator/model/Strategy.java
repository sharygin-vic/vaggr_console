package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.vo.Vacancy;

import java.util.List;

/**
 * Created by dell on 09-Jul-17.
 */
public interface Strategy {
    List<Vacancy> getVacancies(String vacancyJobString, String vacancyLocationName);
    String getVacanciesSourceName();
}

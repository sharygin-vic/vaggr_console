package com.github.vacancy_aggregator.model;

public class VacanciesSearchCommand {
    private String vacancyJobString;
    private String vacancyLocationName;

    public VacanciesSearchCommand(String vacancyLocationName, String vacancyJobString) {
        this.vacancyJobString = vacancyJobString;
        this.vacancyLocationName = vacancyLocationName;
    }

    public String getVacancyJobString() {
        return vacancyJobString;
    }

    public void setVacancyJobString(String vacancyJobString) {
        this.vacancyJobString = vacancyJobString;
    }

    public String getVacancyLocationName() {
        return vacancyLocationName;
    }

    public void setVacancyLocationName(String vacancyLocationName) {
        this.vacancyLocationName = vacancyLocationName;
    }
}

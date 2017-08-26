package com.github.vacancy_aggregator.services;

import com.github.vacancy_aggregator.model.VacanciesSearchCommand;

import java.util.List;

public interface VacanciesSearchCommandsProvider {
    List<VacanciesSearchCommand> getCommands();
}

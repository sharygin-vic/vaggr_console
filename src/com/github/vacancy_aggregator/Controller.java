package com.github.vacancy_aggregator;

import com.github.vacancy_aggregator.model.Model;
import com.github.vacancy_aggregator.model.VacanciesSearchCommand;

import java.util.List;


/**
 * Created by dell on 09-Jul-17.
 */
public class Controller {
    private Model model;

    public Controller(Model model) {
        if (model == null) {
            throw new IllegalArgumentException();
        }
        this.model = model;
    }

    public void onSearchParamsChanged(VacanciesSearchCommand vacanciesSearchCommand) {
        model.searchParamsChanged(vacanciesSearchCommand);
    }

    public void onSearchParamsChanged(List<VacanciesSearchCommand> commands) {
        for (VacanciesSearchCommand c : commands) {
            model.searchParamsChanged(c);
        }
    }
}

package com.github.vacancy_aggregator;

import com.github.vacancy_aggregator.model.Model;


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

    public void onSearchParamsChanged(String vacancySearchString, String vacancyLocationName) {
        model.searchParamsChanged(vacancySearchString, vacancyLocationName);
    }
}

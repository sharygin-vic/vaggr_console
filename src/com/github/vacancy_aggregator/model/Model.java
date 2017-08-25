package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.view.View;
import com.github.vacancy_aggregator.vo.Vacancy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 11-Jul-17.
 */
public class Model {
    private View view;
    private List<Provider> providers;

    public Model(View view, List<Provider> providers) {
        if (view == null || providers == null || providers.size() == 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void searchParamsChanged(String vacancyJobString, String vacancyLocationName) {
        List<Vacancy> vacancies = new ArrayList<Vacancy>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.getJavaVacancies(vacancyJobString, vacancyLocationName));
        }
        view.update(vacancies, vacancyJobString, vacancyLocationName);
    }
}

package com.github.vacancy_aggregator.model;

import com.github.vacancy_aggregator.view.View;
import com.github.vacancy_aggregator.vo.Vacancy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        sortDefault(vacancies);
        view.update(vacancies, vacancyJobString, vacancyLocationName);
    }

    public void sortDefault(List<Vacancy> vacancies) {
        Collections.sort(vacancies, new Comparator<Vacancy>() {
            @Override
            public int compare(Vacancy o1, Vacancy o2) {
                int result;

                result = o1.getCompanyName().compareToIgnoreCase(o2.getCompanyName());
                if (result != 0) return result;

                result = - o1.getVacancyDate().compareTo(o2.getVacancyDate());
                if (result != 0) return result;

                result = o1.getTitle().compareToIgnoreCase(o2.getTitle());
                if (result != 0) return result;

                result = o1.getCity().compareToIgnoreCase(o2.getCity());
                return result;
            }
        });
    }
}

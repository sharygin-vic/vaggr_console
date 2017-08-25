package com.github.vacancy_aggregator.view;

import com.github.vacancy_aggregator.Controller;
import com.github.vacancy_aggregator.vo.Vacancy;
import java.util.List;

/**
 * Created by dell on 11-Jul-17.
 */
public interface View {
    void update(List<Vacancy> vacancies, String vacancyJobString, String vacancyLocationName);
    void setController(Controller controller);
}

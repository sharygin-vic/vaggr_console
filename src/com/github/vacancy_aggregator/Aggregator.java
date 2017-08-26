package com.github.vacancy_aggregator;


import com.github.vacancy_aggregator.model.*;
import com.github.vacancy_aggregator.services.*;
import com.github.vacancy_aggregator.view.HtmlView;
import com.github.vacancy_aggregator.view.View;


import java.util.List;


/**
 * Created by dell on 09-Jul-17.
 */
public class Aggregator {
    public static void main(String[] args) {
        Aggregator app = new Aggregator();

        View view = new HtmlView();
        StrategiesProvider strategiesProvider = new StrategiesProviderFromFile();
        Model model = new Model(view, strategiesProvider.getProviders());
        Controller controller = new Controller(model);
        view.setController(controller);

        VacanciesSearchCommandsProvider commandProvider = new VacanciesSearchCommandsProviderFromFile();
        List<VacanciesSearchCommand> searchCommands = commandProvider.getCommands();
        ((HtmlView)view).userSearchParamsChangedEmulationMethod(searchCommands);
        System.out.println("HTML files were placed in a folder " + PathHelper.getHtmlResultAbsolutePathString());
    }

}

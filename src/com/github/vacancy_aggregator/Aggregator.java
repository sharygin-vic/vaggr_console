package com.github.vacancy_aggregator;


import com.github.vacancy_aggregator.model.*;
import com.github.vacancy_aggregator.services.PathHelper;
import com.github.vacancy_aggregator.view.HtmlView;
import com.github.vacancy_aggregator.view.View;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by dell on 09-Jul-17.
 */
public class Aggregator {
    public static void main(String[] args) {
        String vacancyJobString = "java";
        String vacancyLocationName = "Odessa";

        View view = new HtmlView();

        Model model = new Model(view, loadStrategyProviders());
        Controller controller = new Controller(model);
        view.setController(controller);

        ((HtmlView)view).userSearchParamsChangedEmulationMethod(vacancyJobString, vacancyLocationName);

        System.out.println("HTML file was placed in a folder " + PathHelper.getHtmlResultAbsolutePathString());
    }

    private static List<Provider> loadStrategyProviders() {
        List<Provider> result = new ArrayList<Provider>();
        try (Reader reader = new BufferedReader(new FileReader(PathHelper.getConfigAbsolutePathString() + "strategies.properties")))
        {
            Properties prop = new Properties();
            prop.load(reader);
            for (Map.Entry<Object, Object> item : prop.entrySet()) {
                if ("on".equals(item.getValue())) {
                    result.add(ProvidersFactory.CreateProvider(item.getKey().toString()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

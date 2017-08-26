package com.github.vacancy_aggregator.services;

import com.github.vacancy_aggregator.model.Provider;
import com.github.vacancy_aggregator.model.ProvidersFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StrategiesProviderFromFile implements StrategiesProvider {

    @Override
    public List<Provider> getProviders() {
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

package com.github.vacancy_aggregator.services;

import com.github.vacancy_aggregator.model.Provider;

import java.util.List;

public interface StrategiesProvider {
    List<Provider> getProviders();
}

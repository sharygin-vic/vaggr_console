package com.github.vacancy_aggregator.model;

public class ProvidersFactory {
    public static Provider CreateProvider(Class strategyClass) {
        return CreateProvider(strategyClass.getSimpleName());
    }

    public static Provider CreateProvider(String strategyClassName) {
        if ("HHStrategy".equals(strategyClassName)) {
            return new Provider(new HHStrategy());
        }
        else if ("MoikrugStrategy".equals(strategyClassName)) {
            return new Provider(new MoikrugStrategy());
        }


        else {
            return null;
        }
    }


}

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
        else if ("WorkUaStrategy".equals(strategyClassName)) {
            return new Provider(new WorkUaStrategy());
        }
        else if ("RabotaUaStrategy".equals(strategyClassName)) {
            return new Provider(new RabotaUaStrategy());
        }
        else if ("TrudComStrategy".equals(strategyClassName)) {
            return new Provider(new TrudComStrategy());
        }
        else if ("NeuvooComUaStrategy".equals(strategyClassName)) {
            return new Provider(new NeuvooComUaStrategy());
        }
        else if ("JobsDouUaStrategy".equals(strategyClassName)) {
            return new Provider(new JobsDouUaStrategy());
        }


        else {
            return null;
        }
    }


}

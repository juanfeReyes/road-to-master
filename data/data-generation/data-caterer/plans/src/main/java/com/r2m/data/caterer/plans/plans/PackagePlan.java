package com.r2m.data.caterer.plans.plans;

import io.github.datacatering.datacaterer.javaapi.api.PlanRun;

import java.util.Map;

public class PackagePlan extends PlanRun {

    {
        var baseTask = postgres(
                "base_postgres",
                "jdbc:postgresql://postgresDb:5432/shipment_db",
                "postgres",
                "password",
                Map.of()
        )
                .table("public", "shipment")
                .fields(
                        field().name("source").expression("#{Address.cityName}"),
                        field().name("destination").expression("#{Address.cityName}")
                )
                .count(count().records(10000000));
        var config = configuration()
                .generatedReportsFolderPath(".")
                .enableUniqueCheck(true);

        execute(config, baseTask);

    }
}

package org.example;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.example.config.YamlLoader;

import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        SparkSession spark = SparkSession.builder()
                .master("local[4]")
                .appName("db-connection")
                .getOrCreate();
        var config = YamlLoader.loadConfig();
        spark.sparkContext().log().info(config.getSourceUrl());
        var shipmentDf = loadTable(spark, "shipment");
        shipmentDf.show();
        writeTable(shipmentDf, "shipment_aux");
    }

    private static Dataset<Row> loadTable(SparkSession spark, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        return spark.read().jdbc("jdbc:postgresql://localhost:5432/shipment_db", table, props);
    }

    private static void writeTable(Dataset<Row> df, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        df.write()
                .mode(SaveMode.Overwrite)
                .jdbc("jdbc:postgresql://localhost:5432/warehouse_db", table, props);
    }
}
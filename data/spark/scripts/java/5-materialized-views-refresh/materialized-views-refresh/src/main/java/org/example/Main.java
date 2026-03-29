package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {


    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession.builder()
                .master("local[4]")
                .appName("db-connection")
                .getOrCreate();

        /**
         * #1 execute refresh concurrently for view
         * Meassure time
         */
        var start = System.nanoTime();
        executeQuery("REFRESH MATERIALIZED VIEW CONCURRENTLY shipment_view");
        var end = System.nanoTime();
        spark.sparkContext().log().info("Refresh -> Execution time was: {}", Duration.ofNanos(end - start).toString());

        /**
         * #2 recreate mat view
         * If any failure check if tmp mat view exists and drop
         *
         */
        var query = """
                create materialized view if not exists shipment_view_temp as
                    select *
                    from shipment
                with data;
                
                begin;
                alter materialized view if exists shipment_view_base rename to shipment_view_bck;
                alter materialized view if exists shipment_view_temp rename to shipment_view_base;
                drop materialized view if exists shipment_view_bck;
                commit;
                """;
        var fallBackQuery = "drop materialized view if exists shipment_view_temp";
        start = System.nanoTime();
        executeQuery(query, fallBackQuery);
        end = System.nanoTime();
        spark.sparkContext().log().info("Drop-Recreate -> Execution time was: {}", Duration.ofNanos(end - start).toString());

    }

    private static void executeQuery(String query) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/shipment_db";
        String user = "postgres";
        String password = "password";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void executeQuery(String query, String fallbackQuery) throws Exception {
        try {
            executeQuery(query);
        }
        catch (Exception e){
            executeQuery(fallbackQuery);
        }
    }
}
package org.example;


import org.apache.spark.sql.*;
import org.example.config.YamlLoader;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static scala.collection.JavaConverters.asScala;

public class Main {
    public static void main(String[] args) throws IOException, AnalysisException, SQLException {

        SparkSession spark = SparkSession.builder()
                .master("local[4]")
                .appName("db-connection")
                .getOrCreate();
        var config = YamlLoader.loadConfig();
        upsertOnTarget(spark);
    }

    /**
     * Store new fields in temp table on target and then upsert on db engine
     *
     * @param spark
     */
    public static void upsertOnTarget(SparkSession spark) throws SQLException {
        var target_temp_table_name = "shipment_temp";
        var sourceDf = loadTable(spark, "shipment_db", "shipment");
        try {
            writeTable(sourceDf, "warehouse_db", target_temp_table_name);
            upsertShipment("warehouse_db", target_temp_table_name);
        } finally {
            deleteTable("warehouse_db", target_temp_table_name);
        }
    }

    /**
     * Merge into requires Iceberg to be performed
     *
     * @param spark
     * @throws AnalysisException
     */
    private static void mergeInto(SparkSession spark) throws AnalysisException {
        var sourceDf = loadTable(spark, "shipment_db", "shipment");
        sourceDf.show();
        var targetDf = loadTable(spark, "warehouse_db", "shipment");
        targetDf.show();
        targetDf.createTempView("shipment_target_view");
        sourceDf.as("shipment_source").mergeInto("shipment_target_view", sourceDf.col("id"))
                .whenMatched().update(asScala(java.util.Map.of(
                        "shipment_target_view.source", sourceDf.col("source"),
                        "shipment_target_view.destination", sourceDf.col("destination")
                )).toMap(null))
                .whenNotMatched().insertAll()
                .merge();
    }

    private static Dataset<Row> loadTable(SparkSession spark, String db, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        return spark.read().jdbc("jdbc:postgresql://localhost:5432/" + db, table, props);
    }

    private static void writeTable(Dataset<Row> df, String db, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        df.write()
                .mode(SaveMode.Overwrite)
                .jdbc("jdbc:postgresql://localhost:5432/" + db, table, props);
    }

    private static void deleteTable(String db, String tableName) throws SQLException {
        var dropSql = "DROP TABLE IF EXISTS " + tableName;
        executeSql(dropSql, "jdbc:postgresql://localhost:5432/" + db);
    }

    private static void upsertShipment(String db, String tempTable) throws SQLException {
        var upsertSql = """
                 INSERT INTO shipment (id, source, destination)
                 (SELECT id, source, destination
                 FROM %s)
                 ON CONFLICT (id)
                 DO UPDATE SET
                     source = EXCLUDED.source,
                     destination = EXCLUDED.destination;
                \s""";
        executeSql(String.format(upsertSql, tempTable), "jdbc:postgresql://localhost:5432/" + db);
    }

    private static void executeSql(String sqlQuery, String url) throws SQLException {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        var conn = DriverManager.getConnection(url, props);
        try {
            var statement = conn.createStatement();
            statement.execute(sqlQuery);
        } finally {
            conn.close();
        }
    }
}
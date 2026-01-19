package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.plans.JoinType;

import java.util.Properties;

import static org.apache.spark.sql.functions.col;

public class Main {

    public static final String SHIPMENTS_ICE_TABLE = "local.shipments_ice";
    public static final String SHIPMENTS_TEMP_VIEW = "shipments_temp_view";

    public static void main(String[] args) {
        String userDirectory = System.getProperty("user.dir");
        System.setProperty("hadoop.home.dir", userDirectory);
        SparkSession spark = SparkSession.builder()
                .master("local[4]")
                .appName("lakehouse-integration")
                .config("spark.sql.catalog.local", "org.apache.iceberg.spark.SparkCatalog")
                .config("spark.sql.catalog.local.type", "hadoop")
                .config("spark.sql.catalog.local.warehouse", "./warehouse1")
                .config("spark.sql.defaultCatalog", "local")
                .getOrCreate();

        if (spark.catalog().tableExists(SHIPMENTS_ICE_TABLE)) {
            mergeTable(spark);
            spark.table(SHIPMENTS_ICE_TABLE).show();
            return;
        }

        createIcebergTable(spark);
        spark.table(SHIPMENTS_ICE_TABLE).show();
    }

    private static void mergeTable(SparkSession spark) {
        spark.sparkContext().log().info("MERGE: merging tables");
        var sourceDF = loadTable(spark, "shipment_db", "shipment");
        deleteRowsNotInSource(spark, sourceDF);
        sourceDF.createOrReplaceTempView(SHIPMENTS_TEMP_VIEW);
        spark.table(SHIPMENTS_TEMP_VIEW).mergeInto(SHIPMENTS_ICE_TABLE, col(SHIPMENTS_ICE_TABLE + ".id").equalTo(col(SHIPMENTS_TEMP_VIEW + ".id")))
                .whenMatched()
                .updateAll()
                .whenNotMatched()
                .insertAll()
                .merge();
    }

    private static void deleteRowsNotInSource(SparkSession spark, Dataset<Row> sourceDF){
        // TODO
        var joinedDF = spark.table(SHIPMENTS_ICE_TABLE).as("iceberg")
                .join(sourceDF.as("source"), "id", "left_outer")
                .where(col("source.destination").isNotNull())
                .select("iceberg.id", "source.source", "source.destination");
        joinedDF.show();
        joinedDF.printSchema();
        spark.table(SHIPMENTS_ICE_TABLE).printSchema();
        // Cannot save in iceberg table
//        joinedDF.write().format("iceberg").mode(SaveMode.Overwrite).save(SHIPMENTS_ICE_TABLE);
    }

    private static void createIcebergTable(SparkSession spark) {
        var sourceDf = loadTable(spark, "shipment_db", "shipment");
        sourceDf.writeTo(SHIPMENTS_ICE_TABLE).using("iceberg").createOrReplace();
    }

    private static Dataset<Row> loadTable(SparkSession spark, String db, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        return spark.read().jdbc("jdbc:postgresql://postgresDb:5432/" + db, table, props);
    }
}
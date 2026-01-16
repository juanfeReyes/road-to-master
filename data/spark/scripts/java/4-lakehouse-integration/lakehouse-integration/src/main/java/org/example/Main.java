package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.analysis.NoSuchTableException;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.List;
import java.util.Properties;

public class Main {

    public static final String SHIPMENTS_ICE_TABLE = "local.shipments_ice";

    public static void main(String[] args) throws NoSuchTableException {
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

        spark.catalog().listCatalogs().show();
        var sourceDf = loadTable(spark, "shipment_db", "shipment");
        sourceDf.show();

        var schema = new StructType(new StructField[]{
                DataTypes.createStructField("id", DataTypes.IntegerType, true),
                DataTypes.createStructField("source", DataTypes.StringType, true),
                DataTypes.createStructField("destination", DataTypes.StringType, true)
        });
        var data = List.of(RowFactory.create(1, "Cali", "Yumbo"));
        var df = spark.createDataFrame(data, schema);
        df.writeTo(SHIPMENTS_ICE_TABLE).createOrReplace();
        spark.table(SHIPMENTS_ICE_TABLE).show();
        var newDF = spark.createDataFrame(sourceDf.collectAsList(), schema);
        newDF.writeTo(SHIPMENTS_ICE_TABLE).using("iceberg").append();
        spark.table(SHIPMENTS_ICE_TABLE).show();
    }

    private static Dataset<Row> loadTable(SparkSession spark, String db, String table) {
        Properties props = new Properties();
        props.put("user", "postgres");
        props.put("password", "password");
        props.put("driver", "org.postgresql.Driver");
        return spark.read().jdbc("jdbc:postgresql://postgresDb:5432/" + db, table, props);
    }
}
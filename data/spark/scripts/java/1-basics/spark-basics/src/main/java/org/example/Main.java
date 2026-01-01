package org.example;


import org.apache.spark.SparkFiles;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Main {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .master("local[4]")
                .appName("basics")
                .getOrCreate();

        var df = loadCsvFromResource(spark, "basics.csv");

        df.show();

        String dir = SparkFiles.getRootDirectory();
        spark.sparkContext().log().info("Starting Job");
        spark.sparkContext().log().info(dir);
    }

    private static Dataset<Row> loadCsvFromResource(SparkSession spark, String fileName){
        String dataPath = ClassLoader.getSystemResource(fileName).getPath();

        return spark.read()
                .format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load(dataPath);
    }
}
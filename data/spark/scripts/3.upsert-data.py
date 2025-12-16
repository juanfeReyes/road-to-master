from pyspark.sql import SparkSession

def build_spark():
    return SparkSession.builder\
    .appName("Shipment ETL")\
    .config("spark.jars", "data/drivers/postgresql-42.7.8.jar")\
    .getOrCreate()

def build_df(spark: SparkSession, db: str, table: str):
    connection_str = f"jdbc:postgresql://postgresDb:5432/{db}"
    properties = {
        "user": "postgres",
        "password": "password",
        "driver": 'org.postgresql.Driver'
    }

    return spark.read.jdbc(
        url=connection_str,
        properties=properties,
        table=table
    )

def upsert_shipment_etl():
    spark = build_spark()
    shipment_df = build_df(spark=spark, db="shipment_db", table="shipment")
    warehouse_shipment_df = build_df(spark=spark, db="warehouse_db", table="shipment")
    


if __name__ == '__main__':
    upsert_shipment_etl()

from pyspark.sql import SparkSession, DataFrame
from pyspark import SparkConf

def build_df(spark: SparkSession, connection_str: str, username: str, password: str, table: str):
    properties = {
        "user": username,
        "password": password,
        "driver": 'org.postgresql.Driver'
    }

    return spark.read.jdbc(
        url=connection_str,
        properties=properties,
        table=table
    )

def write_data(data_df: DataFrame, connection_str: str, username: str, password: str, table: str):
    properties = {
        "user": username,
        "password": password,
        "driver": 'org.postgresql.Driver'
    }
    data_df.write.jdbc(url=connection_str, table=table, properties=properties)

def etl():
    spark = SparkSession.builder\
    .appName("Shipment ETL")\
    .config("spark.jars", "data/drivers/postgresql-42.7.8.jar")\
    .getOrCreate()

    shipment_df: DataFrame = build_df(spark=spark, connection_str="jdbc:postgresql://postgresDb:5432/shipment_db", username="postgres", password="password", table="shipment")
    shipment_df.show()
    write_data(shipment_df, connection_str="jdbc:postgresql://postgresDb:5432/warehouse_db", username="postgres", password="password", table="shipment_aux")
    
    
if __name__ == "__main__":
    etl()
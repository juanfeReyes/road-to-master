from pyspark.sql import SparkSession

def counting():
    spark = SparkSession.builder.getOrCreate()
    count = spark.range(1000 * 1000 * 1000).count()
    print("MY FIRST LOG")
    print(f"Counting result: {count}")

if __name__ == "__main__":
    counting()


output "aws_s3_object_bucket" {
  value = aws_s3_bucket.lambda_store.bucket
  description = "Bucket of the lambda source"
}


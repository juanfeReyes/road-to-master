
output "aws_s3_object_bucket" {
  value = aws_s3_bucket.lambda_source.bucket
  description = "Bucket of the lambda source"
}


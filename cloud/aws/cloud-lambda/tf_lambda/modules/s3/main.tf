resource "aws_s3_bucket" "lambda_source" {
  bucket = var.bucket_name

  force_destroy = true

  tags = {
    Environment = "dev"
  }
}

resource "aws_s3_object" "lambda_zip" {
  bucket = aws_s3_bucket.lambda_source.bucket
  key = "r2m_lambda.zip"
  source = var.bucket_source 
}

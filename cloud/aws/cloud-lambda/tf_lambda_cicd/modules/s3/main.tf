resource "aws_s3_bucket" "lambda_store" {
  bucket = var.bucket_name

  force_destroy = true

  tags = {
    Environment = "dev"
  }
}


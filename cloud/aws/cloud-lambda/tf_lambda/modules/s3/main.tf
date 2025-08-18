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

# Permissions to allow lamda service to access the source bucket
# resource "aws_s3_bucket_policy" "lambda_source_policy" {
#   bucket = aws_s3_bucket.lambda_source.id
#   policy = data.aws_iam_policy_document.allow_access_lambda_source.json
# }

# data "aws_iam_policy_document" "allow_access_lambda_source" {
#   statement {
#     principals {
#       type = "AWS"
#       identifiers = [ "" ] #TODO: identifier
#     }

#     actions = [
#       "s3:*"
#     ]

#     resources = [ 
#       aws_s3_bucket.lambda_source.arn
#      ]
#   }
# }

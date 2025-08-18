
module "iam" {
  source = "./modules/iam"
}

module "s3" {
  source = "./modules/s3"

  bucket_name = "r2m-lambda-source"
  bucket_source = var.bucket_source
}

module "lambda" {
  source = "./modules/lambda"
  
  execution_role_name = module.iam.lambda_execution_role_name
  lambda_bucket = module.s3.aws_s3_object_bucket
  lambda_object_key = module.s3.aws_s3_object_key
}


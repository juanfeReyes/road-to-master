
module "iam" {
  source = "./modules/iam"
}

module "s3" {
  source = "./modules/s3"
}

# module "lambda" {
#   source = "./modules/lambda"

#   execution_role_name = module.iam.lambda_execution_role_name
#   lambda_bucket = module.s3.aws_s3_object_bucket
#   lambda_object_key = module.s3.aws_s3_object_key
# }

module "codepipeline" {
  source = "./modules/codepipeline"

  codebuild_role_name = module.iam.codebuild_role_name
  codepipeline_role_name = module.iam.codepipeline_role_name
  codepipeline_bucket = module.s3.aws_s3_object_bucket
  lambda_repo_folder_path = var.repo_path
  connection_arn = var.connectionArn
  lambda_name = "r2m_function_test"
}

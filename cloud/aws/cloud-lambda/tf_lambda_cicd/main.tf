
module "iam" {
  source = "./modules/iam"
}

module "s3" {
  source = "./modules/s3"
}

module "codepipeline" {
  source = "./modules/codepipeline"

  codebuild_role_name = module.iam.codebuild_role_name
  codepipeline_role_name = module.iam.codepipeline_role_name
  codepipeline_bucket = module.s3.aws_s3_object_bucket
  connection_arn = var.connectionArn
  lambda_name = var.lambda_name
}

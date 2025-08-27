
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
  lambda_repo_folder_path = var.repo_path
  connection_arn = var.connectionArn
}

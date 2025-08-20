
module "iam" {
  source = "./modules/iam"
}

module "s3" {
  source = "./modules/s3"
}

module "codepipeline" {
  source = "./modules/codepipeline"
}

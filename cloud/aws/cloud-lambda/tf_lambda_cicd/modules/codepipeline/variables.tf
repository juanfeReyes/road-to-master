
variable "codepipeline_role_name" {
  type = string
  description = "Codepipeline role name"
}

variable "codebuild_role_name" {
  type = string
  description = "Codebuild role name"
}

variable "codepipeline_bucket" {
  type = string
  description = "Codepipeline bucket storage"
}

variable "lambda_repo_folder_path" {
  type = string
  description = "Path of the lambda source code to build in the repo"
}

variable "connection_arn" {
  type = string
  description = "Codeconnection ARN"
}

variable "lambda_name" {
  type = string
  description = "Name of the Lambda function"
}

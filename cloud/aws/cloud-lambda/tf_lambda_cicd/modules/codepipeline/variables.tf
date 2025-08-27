
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

variable "connection_arn" {
  type = string
  description = "Codeconnection ARN"
}

variable "lambda_name" {
  type = string
  description = "Name of the Lambda function"
}

variable "source_branch" {
  type = string
  description = "Branch name for the source"
  default = "main"
}

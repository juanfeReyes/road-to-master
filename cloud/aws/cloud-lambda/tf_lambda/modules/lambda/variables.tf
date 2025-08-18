
variable "execution_role_name" {
  type = string
  description = "Id of the IAM role for the lambda execution permissions"
}

variable "lambda_bucket" {
  type = string
  description = "Bucket where the lambda is stored"
}

variable "lambda_object_key" {
  type = string
  description = "Key of the S3 object for lambda artifact"
}

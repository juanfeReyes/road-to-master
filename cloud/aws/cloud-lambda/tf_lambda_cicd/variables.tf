
variable "repo_path" {
  type = string
  description = "Path of the folder of the folder in the repo that contains Lambda source code"
}

variable "connectionArn" {
  type = string
  description = "ARN for the CodeConnection ARN"
}

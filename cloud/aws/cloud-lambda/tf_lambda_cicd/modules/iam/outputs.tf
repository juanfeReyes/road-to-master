
output "lambda_execution_role_name" {
  value = aws_iam_role.lambda_role.name  
  description = "ARN for the execution role to assing permissions to lambda"
}

output "codebuild_role_name" {
  value = aws_iam_role.codebuild_role.name  
  description = "Name of the codebuild role"
}

output "codepipeline_role_name" {
  value = aws_iam_role.codepipeline_role.name  
  description = "Name of the coddepipeline role"
}

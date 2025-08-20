
output "lambda_execution_role_name" {
  value = aws_iam_role.lambda_role.name  
  description = "ARN for the execution role to assing permissions to lambda"
}

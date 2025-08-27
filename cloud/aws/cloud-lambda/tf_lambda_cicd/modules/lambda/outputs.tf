output "lambda_name" {
  value = aws_lambda_function.func.function_name
  description = "Lambda function name"
}


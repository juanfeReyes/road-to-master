
data "aws_iam_role" "name" {
  name = var.execution_role_name

  depends_on = [ var.execution_role_name ]
}

resource "aws_lambda_function" "func" {
  function_name = "r2m_lambda"

  role = data.aws_iam_role.name.arn
  s3_bucket = var.lambda_bucket
  s3_key = var.lambda_object_key 

  handler = "helloworld.App::handleRequest"
  runtime = "java21"

  environment {
    variables = {
      ENV_NAME = "dev_cloud"
    }
  }

  tags = {
    environment = "dev_cloud"
  }
}

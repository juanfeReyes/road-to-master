
data "aws_iam_policy_document" "assume_role" {
  statement {
    sid = "1"
    effect = "Allow"
    principals {
      type = "Service"
      identifiers = [ "lambda.amazonaws.com" ]
    }

    actions = [ 
      "sts:AssumeRole"
     ]
  }
}

resource "aws_iam_role" "lambda_role" {
  name = "lambda_execution_role"
  assume_role_policy = data.aws_iam_policy_document.assume_role.json
}

# resource "aws_iam_policy" "lambda" {
#   name = "lambda_policy"
#   path = "/"
#   policy = aws_iam_policy_document.policy_document.json
# }

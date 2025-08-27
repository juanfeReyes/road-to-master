
################################################################################
##       LAMBDA
################################################################################
data "aws_iam_policy_document" "lambda_assume_role" {
  statement {
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["lambda.amazonaws.com"]
    }

    actions = [
      "sts:AssumeRole"
    ]
  }
}

resource "aws_iam_role" "lambda_role" {
  name               = "lambda_execution_role"
  assume_role_policy = data.aws_iam_policy_document.lambda_assume_role.json
}

################################################################################
##       CODEPIPELINE
################################################################################

data "aws_iam_policy_document" "codepipeline_assume_role" {
  statement {
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["codepipeline.amazonaws.com"]
    }

    actions = [
      "sts:AssumeRole"
    ]
  }
}

resource "aws_iam_role" "codepipeline_role" {
  name               = "r2m_codepipeline-role"
  assume_role_policy = data.aws_iam_policy_document.codepipeline_assume_role.json
}

data "aws_iam_policy_document" "lambda_cd" {
  statement {
    effect = "Allow"
    actions = [
      "codeconnections:UseConnection",
      "codestar-connections:UseConnection",
      "s3:*",
      "codebuild:*"
    ]
    resources = [
      "*"
    ]
  }
}

resource "aws_iam_policy" "lambda_cd" {
  name   = "lambda_cd_policy"
  policy = data.aws_iam_policy_document.lambda_cd.json
}

resource "aws_iam_role_policy_attachment" "lambda_cd_codepipeline_rola" {
  role       = aws_iam_role.codepipeline_role.name
  policy_arn = aws_iam_policy.lambda_cd.arn
}


################################################################################
##       CODEBUILD
################################################################################

data "aws_iam_policy_document" "code_build" {
  statement {
    effect = "Allow"
    principals {
      type        = "Service"
      identifiers = ["codebuild.amazonaws.com"]
    }

    actions = [
      "sts:AssumeRole"
    ]
  }
}

resource "aws_iam_role" "codebuild_role" {
  name               = "codebuild_role"
  assume_role_policy = data.aws_iam_policy_document.code_build.json
}

data "aws_iam_policy_document" "lambda_build_cd" {
  statement {
    effect = "Allow"
    actions = [
      "s3:*",
      "logs:*",
      "codeconnections:*",
      "codestar-connections:UseConnection"
    ]
    resources = [
      "*"
    ]
  }
}

resource "aws_iam_policy" "lambda_build_cd" {
  name   = "lambda_build_policy"
  policy = data.aws_iam_policy_document.lambda_build_cd.json
}

resource "aws_iam_role_policy_attachment" "lambda_build_cd_codepipeline_rola" {
  role       = aws_iam_role.codebuild_role.name
  policy_arn = aws_iam_policy.lambda_build_cd.arn
}


data "aws_iam_role" "codepipeline_role" {
  name = var.codepipeline_role_name

  depends_on = [var.codepipeline_role_name]
}

data "aws_iam_role" "codebuild_role" {
  name = var.codebuild_role_name

  depends_on = [var.codebuild_role_name]
}

resource "aws_codebuild_project" "lambda_build" {
  name         = "lambda-build"
  description  = "Code build project to build Lambda"
  service_role = data.aws_iam_role.codebuild_role.arn

  artifacts {
    type = "CODEPIPELINE"
  }

  environment {
    compute_type                = "BUILD_GENERAL1_SMALL"
    image                       = "aws/codebuild/amazonlinux2-x86_64-standard:4.0"
    type                        = "LINUX_CONTAINER"
    image_pull_credentials_type = "CODEBUILD"
  }

  source {
    type      = "CODEPIPELINE"
    buildspec = "buildspec-build.yml"
  }

  # Explore second source and specify buildspec
}

resource "aws_codepipeline" "deploy_lambda" {
  name          = "r2m_lambda_pipeline"
  pipeline_type = "V2"

  role_arn = data.aws_iam_role.codepipeline_role.arn

  artifact_store {
    location = var.codepipeline_bucket
    type     = "S3"
  }

  trigger {
    provider_type = "CodeStarSourceConnection"
    git_configuration {
      source_action_name = "Source"
      push {

        file_paths {
          includes = [var.lambda_repo_folder_path]
        }
        branches {
          includes = ["main"]
        }
      }

    }
  }

  stage {
    name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeStarSourceConnection"
      version          = "1"
      output_artifacts = ["source_lambda"]

      configuration = {
        ConnectionArn    = var.connection_arn # add connection ARN from codeconnections
        FullRepositoryId = "juanfeReyes/road-to-master"
        BranchName       = "main"
      }
    }
  }

  stage {
    name = "Build"

    action {
      name             = "Lambda-Build"
      category         = "Build"
      owner            = "AWS"
      provider         = "CodeBuild"
      version          = "1"
      input_artifacts  = ["source_lambda"]
      output_artifacts = ["lambda_artifact"]

      configuration = {
        ProjectName = aws_codebuild_project.lambda_build.name # Name of the the code build project
      }
    }
  }

  # stage {
  #   name = "Deploy to Lambda"
  # }
}

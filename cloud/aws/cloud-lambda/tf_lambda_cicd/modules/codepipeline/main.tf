
data "aws_iam_role" "name" {
  name = var.codepipeline_role_name
}

resource "aws_codepipeline" "deploy_lambda" {
  name = "r2m_lamnda_pipeline"

  role_arn = data.aws_iam_role.name.arn

  artifact_store {
    location = var.codepipeline_bucket
    type = "S3"
  }

  stage {
    name = "Source"

    action {
      
    }
  }

  stage {
    
  }
}

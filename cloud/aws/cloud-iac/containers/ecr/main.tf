
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.93.0"
    }
    ansible = {
      source  = "ansible/ansible"
      version = "1.3.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

// Create private ECR repository (Run this before the rest) (how to run in multiple steps)
resource "aws_ecr_repository" "containers_repository" {
  name = "r2m-containers-repository"
  force_delete = true
}

data "aws_iam_policy_document" "container_policy" {
  statement {
    sid = "R2M-containers-repository-policy"
    effect = "Allow"

    principals {
      type = "AWS"
      identifiers = [ "arn:aws:iam::140023377333:role/ecsInstanceRole" ]
    }

    actions = [ 
      "ecr:BatchCheckLayerAvailability",
      "ecr:BatchGetImage",
      "ecr:DescribeImages",
      "ecr:GetAuthorizationToken",
      "ecr:ListImages"
     ]
  }

  version = "2012-10-17"
}

// TODO add policy for role of EC2 instance
resource "aws_ecr_repository_policy" "containers_repository_policy" {
  repository = aws_ecr_repository.containers_repository.name
  policy = data.aws_iam_policy_document.container_policy.json
}

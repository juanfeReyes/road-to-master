
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

module "containers_vpc" {
  source = "./modules/vpc"
}

module "ec2_config" {
  source = "./modules/ec2_setup"

  vpc_id           = module.containers_vpc.vpc_id
  public_subnet_ids = module.containers_vpc.public_subnet_ids
}

module "ecs_cluster" {
  source = "./modules/ecs"

  auto_scaling_group_arn = module.ec2_config.auto_scaling_group_arn
  auto_scaling_group_depends_on = module.ec2_config.auto_scaling_group
  private_web_sg_id = module.ec2_config.private_web_sg_id
  private_api_sg_id = module.ec2_config.private_api_sg_id
  public_subnet_ids = module.containers_vpc.public_subnet_ids
  private_subnet_ids = module.containers_vpc.private_subnet_ids
  lb_target_group_arn = module.ec2_config.lb_tg_arn
  service_discovery_arn = module.ec2_config.service_discovery_arn
}



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
    # ansible = {
    #   source  = "habakke/ansible"
    #   version = "2.0.1"
    # }
  }
}

provider "aws" {
  region = "us-east-1"
}

// Infrastructure config

resource "aws_vpc" "multitier_web_vpc" {
  cidr_block           = "10.16.0.0/16"
  enable_dns_hostnames = true
}

// Subnet configuration

resource "aws_subnet" "basic_web_public_subnet" {
  vpc_id                  = aws_vpc.multitier_web_vpc.id
  cidr_block              = "10.16.0.0/24"
  map_public_ip_on_launch = true
  tags = {
    develop = "basic"
  }
}

resource "aws_network_acl" "public_nacl" {
  vpc_id     = aws_vpc.multitier_web_vpc.id
  subnet_ids = [aws_subnet.basic_web_public_subnet.id]

  ingress {
    from_port  = 0
    to_port    = 0
    rule_no    = 100
    protocol   = -1 //all
    action     = "allow"
    cidr_block = "0.0.0.0/0"
  }

  egress {
    from_port  = 0
    to_port    = 0
    rule_no    = 100
    protocol   = -1 //all
    action     = "allow"
    cidr_block = "0.0.0.0/0"
  }

}

resource "aws_route_table" "basic_web_public_rt" {
  vpc_id = aws_vpc.multitier_web_vpc.id
}

resource "aws_route_table_association" "basic_web_public_rt_assoc" {
  route_table_id = aws_route_table.basic_web_public_rt.id
  subnet_id      = aws_subnet.basic_web_public_subnet.id
}

// Internet Gateway config

resource "aws_internet_gateway" "basic_web_igw" {
  vpc_id = aws_vpc.multitier_web_vpc.id
}

resource "aws_route" "igw_route" {
  route_table_id         = aws_route_table.basic_web_public_rt.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.basic_web_igw.id
}

// Bastion-NAT configuration

resource "aws_security_group" "nat_bastion_sg" {
  name        = "nat_bastion_sg"
  description = "Allow ssh and http"
  vpc_id      = aws_vpc.multitier_web_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["181.234.158.187/32"] // delete myIp
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

module "fck-nat" {
  source = "RaJiska/fck-nat/aws"

  name          = "bastion_nat"
  vpc_id        = aws_vpc.multitier_web_vpc.id
  subnet_id     = aws_subnet.basic_web_public_subnet.id
  ha_mode       = false
  instance_type = "t2.micro"
  additional_security_group_ids = [
    aws_security_group.nat_bastion_sg.id
  ]
  use_ssh      = true
  ssh_key_name = "r2m-cloud-key"
}

data "aws_network_interface" "bastion_eni" {
  id = module.fck-nat.eni_id
}

/**
Instance configuration
*/

resource "aws_security_group" "public_web_sg" {
  name        = "public_web_sg"
  description = "Allow ssh and http"
  vpc_id      = aws_vpc.multitier_web_vpc.id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = module.fck-nat.security_group_ids //Security groups can be sg sources
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "web_instance" {
  ami           = "ami-08b5b3a93ed654d19"
  instance_type = "t2.micro"
  tags = {
    develop = "basic"
  }
  subnet_id              = aws_subnet.basic_web_public_subnet.id
  vpc_security_group_ids = [aws_security_group.public_web_sg.id]
  key_name               = "r2m-cloud-key"
}

// Ansible inventory

resource "ansible_group" "web" {
  name     = "web_instances"
  children = ["web"]
  variables = {
    ansible_ssh_common_args =  "-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ProxyCommand='ssh -A -W %h:%p -q ec2-user@${module.fck-nat.instance_public_ip}'"
  }
}

resource "ansible_host" "web-app" {
  name   = aws_instance.web_instance.private_ip
  groups = ["web"]
  variables = {
    ansible_host                 = aws_instance.web_instance.private_ip
    ansible_user                 = "ec2-user"
    ansible_ssh_private_key_file = "/mnt/d/road to master/r2m-cloud-key.pem"
  }
}

// Api configuration 

resource "aws_security_group" "private_api_sg" {
  name        = "private_api_sg"
  description = "Allow ssh and http"
  vpc_id      = aws_vpc.multitier_web_vpc.id
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "api_instance" {
  ami           = "ami-08b5b3a93ed654d19"
  instance_type = "t2.micro"
  tags = {
    develop = "basic"
  }
  subnet_id              = aws_subnet.basic_web_private_subnet
  vpc_security_group_ids = [aws_security_group.private_api_sg.id]
  key_name               = "r2m-cloud-key"
  private_ip = "10.16.1.10"
}

resource "ansible_group" "api" {
  name     = "api_instances"
  children = ["api"]
  variables = {
    ansible_ssh_common_args =  "-o StrictHostKeyChecking=no -o UserKnownHostsFile=/dev/null -o ProxyCommand='ssh -A -W %h:%p -q ec2-user@${module.fck-nat.instance_public_ip}'"
  }
}

resource "ansible_host" "web-app" {
  name   = aws_instance.api_instance.private_ip
  groups = ["api"]
  variables = {
    ansible_host                 = aws_instance.api_instance.private_ip
    ansible_user                 = "ec2-user"
    ansible_ssh_private_key_file = "/mnt/d/road to master/r2m-cloud-key.pem"
  }
}

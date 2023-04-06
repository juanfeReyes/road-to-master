locals {
  vpc_id           = "vpc-078899cc6798ed185"
  subnet_id        = "subnet-0538a3b8caa2bfc18"
  ssh_user         = "ubuntu"
  key_name         = "pet_key"
  private_key_path = "/home/juan/pet_key.pem"
  playbook_path    = "/mnt/d/Development/road to master/pet-infrastructure/playbooks/pet_api_playbook.yaml"
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.27"
    }
  }

  required_version = ">= 0.14.9"
}

provider "aws" {
  profile = "default"
  region  = "us-east-1"
}

resource "aws_security_group" "pet_security_group" {
  name   = "pet_api_access"
  vpc_id = local.vpc_id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

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

// Do a AWS migration version of the api, with lighter features
resource "aws_instance" "pet_api" {
  // Ubuntu image
  ami           = "ami-04505e74c0741db8d"
  subnet_id     = local.subnet_id
  //t2.micro is to small to run the provisioning script
  instance_type = "t2.micro"
  associate_public_ip_address = true
  security_groups             = [aws_security_group.pet_security_group.id]
  key_name                    = local.key_name

  tags = {
    "Name" = "pet api"
  }

  provisioner "remote-exec" {
    inline = ["echo 'Wait until SSH is ready'"]

    connection {
      type        = "ssh"
      user        = local.ssh_user
      private_key = file(local.private_key_path)
      host        = aws_instance.pet_api.public_ip
    }
  }
  // Provision to start compose in instance
  //provisioner "local-exec" {
  //  command = "ansible-playbook  -i ${aws_instance.pet_api.public_ip}, --private-key ${local.private_key_path} pet-api-playbook.yaml"
  //}

}

output "pet_api_ip" {
  value = aws_instance.pet_api.public_ip
}

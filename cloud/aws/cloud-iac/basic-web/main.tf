// Basic web app config

provider "aws" {
  region = "us-east-1"
}

resource "aws_vpc" "basic_web_vpc" {
  cidr_block           = "10.16.0.0/16"
  enable_dns_hostnames = true
}

// Subnet configuration

resource "aws_subnet" "basic_web_public_subnet" {
  vpc_id                  = aws_vpc.basic_web_vpc.id
  cidr_block              = "10.16.0.0/24"
  map_public_ip_on_launch = true
  tags = {
    develop = "basic"
  }
}

resource "aws_network_acl" "public_nacl" {
  vpc_id     = aws_vpc.basic_web_vpc.id
  subnet_ids = [aws_subnet.basic_web_public_subnet.id]

  ingress {
    from_port = 0
    to_port   = 0
    rule_no   = 100
    protocol  = -1 //all
    action    = "allow"
    cidr_block = "0.0.0.0/0"
  }

  egress {
    from_port = 0
    to_port   = 0
    rule_no   = 100
    protocol  = -1 //all
    action    = "allow"
    cidr_block = "0.0.0.0/0"
  }

}

resource "aws_route_table" "basic_web_public_rt" {
  vpc_id = aws_vpc.basic_web_vpc.id
}

resource "aws_route_table_association" "basic_web_public_rt_assoc" {
  route_table_id = aws_route_table.basic_web_public_rt.id
  subnet_id = aws_subnet.basic_web_public_subnet.id
}


// Internet Gateway config

resource "aws_internet_gateway" "basic_web_igw" {
  vpc_id = aws_vpc.basic_web_vpc.id
}

resource "aws_route" "igw_route" {
  route_table_id         = aws_route_table.basic_web_public_rt.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.basic_web_igw.id
}

/**
Instance configuration
*/

resource "aws_security_group" "private_web_sg" {
  name        = "private_web_sg"
  description = "Allow ssh and http"
  vpc_id      = aws_vpc.basic_web_vpc.id
  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["x.x.x.x/32"] // delete myIp
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
  user_data              = file("basic-web-provision.tpl")
  subnet_id              = aws_subnet.basic_web_public_subnet.id
  vpc_security_group_ids = [aws_security_group.private_web_sg.id]
  key_name               = "r2m-cloud-key"
}

output "web_instance_public_dns" {
  value = aws_instance.web_instance.public_dns
}


// Create VPC and subnets (private and public)
resource "aws_vpc" "containers_vpc" {
  cidr_block           = "10.16.0.0/16"
  enable_dns_hostnames = true
}

resource "aws_subnet" "public_subnet_a" {
  vpc_id                  = aws_vpc.containers_vpc.id
  availability_zone = "us-east-1a"
  cidr_block              = cidrsubnet(aws_vpc.containers_vpc.cidr_block, 8, 0)
  map_public_ip_on_launch = true
  tags = {
    domain = "containers",
    env = "dev"
  }
}

resource "aws_subnet" "public_subnet_b" {
  vpc_id                  = aws_vpc.containers_vpc.id
  availability_zone = "us-east-1b"
  cidr_block              = cidrsubnet(aws_vpc.containers_vpc.cidr_block, 8, 1)
  map_public_ip_on_launch = true
  tags = {
    domain = "containers",
    env = "dev"
  }
}

resource "aws_network_acl" "public_nacl" {
  vpc_id     = aws_vpc.containers_vpc.id
  subnet_ids = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]

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

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.containers_vpc.id
  tags = {
    Name = "containers_rt"
  }
}

resource "aws_route_table_association" "public_rt_association_a" {
  route_table_id = aws_route_table.public_rt.id
  subnet_id      = aws_subnet.public_subnet_a.id
}

resource "aws_route_table_association" "public_rt_association_b" {
  route_table_id = aws_route_table.public_rt.id
  subnet_id      = aws_subnet.public_subnet_b.id
}

resource "aws_subnet" "private_subnet" {
  vpc_id = aws_vpc.containers_vpc.id
  cidr_block              = cidrsubnet(aws_vpc.containers_vpc.cidr_block, 8, 2)
  tags = {
    domain = "containers",
    env = "dev"
  }
}

// Internet Gateway
resource "aws_internet_gateway" "internet_gw" {
  vpc_id = aws_vpc.containers_vpc.id
  tags = {
    env = "dev"
  }
}

resource "aws_route" "igw_route" {
  route_table_id         = aws_route_table.public_rt.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.internet_gw.id
}

// TODO: add route 

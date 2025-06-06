// Create EC2 config
data "http" "myip" {
  url = "https://ipv4.icanhazip.com"
}


// TODO: Fix sg config by: https://nav7neeet.medium.com/load-balance-traffic-to-private-ec2-instances-cb07058549fd
resource "aws_security_group" "private_web_sg" {
  name        = "private_web_sg"
  description = "Allow http"
  vpc_id      = var.vpc_id
  ingress {
    from_port   = 80
    to_port     = 80
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

resource "aws_security_group" "private_api_sg" {
  name        = "private_api_sg"
  description = "Allow http"
  vpc_id      = var.vpc_id
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

// Setup auto scaling group

data "aws_ami" "amazon_linux_2" {
  most_recent = true

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  filter {
    name   = "owner-alias"
    values = ["amazon"]
  }

  filter {
    name   = "name"
    values = ["amzn2-ami-ecs-hvm-*-x86_64-ebs"]
  }

  owners = ["amazon"]
}

resource "aws_launch_template" "ec2_lt" {
  name_prefix = "ecs_template"
  image_id = data.aws_ami.amazon_linux_2.id // Find ECS optimized AMI
  instance_type = "t2.micro"

  key_name               = "r2m-cloud-key"
  vpc_security_group_ids = [aws_security_group.private_web_sg.id]

  iam_instance_profile {
    name = "ecsInstanceRole"
  }

  block_device_mappings {
    device_name = "/dev/xvda"
    ebs {
      volume_size = 30
      volume_type = "gp2"
    }
  }
  
  tag_specifications {
    resource_type = "instance"
    tags = {
      Name = "ecs-instance"
      env = "dev"
    }
  }

  user_data = filebase64("${path.module}/ecs.sh")
}

resource "aws_autoscaling_group" "ecs_asg" {
  vpc_zone_identifier = var.public_subnet_ids
  desired_capacity = 2
  max_size = 2
  min_size = 1

  launch_template {
    id = aws_launch_template.ec2_lt.id
    version = "$Latest"
  }

  tag {
    key = "AmazonECSManaged"
    value = true
    propagate_at_launch = true
  }

  provisioner "local-exec" {
    when = destroy
    command = <<EOF
      echo "Update ASG to scale down to 0"
      aws.exe autoscaling update-auto-scaling-group --region us-east-1  --auto-scaling-group-name ${self.name} --min-size 0 --desired-capacity 0
      echo "Update ASG to scale down to 0 Completed"
    EOF
  }
}

// Create Application Load Balancer

resource "aws_lb" "ecs_lb" {
  name = "ecs-lb"
  internal = false
  load_balancer_type = "application"
  security_groups = [aws_security_group.private_web_sg.id]
  subnets = var.public_subnet_ids

  tags = {
    Name = "ecs-alb"
  }
}

resource "aws_lb_target_group" "ecs_lb_target_group" {
  name = "ecs-lb-tg"
  port = 80
  protocol = "HTTP"
  target_type = "ip"
  vpc_id = var.vpc_id

  health_check {
    protocol = "HTTP"
    path = "/" // Test the path for the health check
  }
}

resource "aws_lb_listener" "ecs_lb_listener" {
  load_balancer_arn = aws_lb.ecs_lb.arn
  protocol = "HTTP"
  port = 80

  default_action {
    type = "forward"
    target_group_arn = aws_lb_target_group.ecs_lb_target_group.arn
  }
}


// Cloud Map Namespace
resource "aws_service_discovery_private_dns_namespace" "containers_service_discovery_namespace" {
  name = "r2m.containers.co"
  description = "Private DNS namespace for service discovery"
  vpc = var.vpc_id
}

resource "aws_service_discovery_service" "containers_service_discovery_service" {
  name = "cloud-api"

  dns_config {
    namespace_id = aws_service_discovery_private_dns_namespace.containers_service_discovery_namespace.id

    dns_records {
      ttl = 10
      type = "A"
    }

    routing_policy = "MULTIVALUE"
  }

  health_check_custom_config {
    failure_threshold = 1
  }


}

// Create ECS cluster
// TODO: fix cluster is not deleted when there are tasks running
resource "aws_ecs_cluster" "containers_cluster" {
  name = "r2m-containers-cluster"

}

resource "aws_ecs_capacity_provider" "containers_cp" {
  name = "r2m-capacity-provider"

  auto_scaling_group_provider {
    auto_scaling_group_arn = var.auto_scaling_group_arn

    managed_scaling {
      maximum_scaling_step_size = 1000
      minimum_scaling_step_size = 1
      status                    = "ENABLED"
      target_capacity           = 2
    }
  }
}

resource "aws_ecs_cluster_capacity_providers" "ecs_ca" {
  cluster_name = aws_ecs_cluster.containers_cluster.name

  capacity_providers = [aws_ecs_capacity_provider.containers_cp.name]
  default_capacity_provider_strategy {
    base              = 1
    weight            = 100
    capacity_provider = aws_ecs_capacity_provider.containers_cp.name
  }
}

// Create Task definition
resource "aws_ecs_task_definition" "ecs_web_td" {
  family             = "containers-web-task"
  network_mode       = "awsvpc"
  execution_role_arn = "arn:aws:iam::140023377333:role/ecsTaskExecutionRole"
  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }
  container_definitions = jsonencode([
    {
      name      = "cloud-ui"
      image     = "140023377333.dkr.ecr.us-east-1.amazonaws.com/r2m-containers-repository:cloud-ui"
      cpu       = 512
      memory    = 512
      essential = true
      portMappings = [ // Docker container not mapping ports
        {
          containerPort = 80
          hostPort      = 80
          protocol      = "tcp"
        }
      ]
      healthCheck = {
        command = [
          "CMD-SHELL",
          "service nginx status || exit 1"
        ]
      }
      logConfiguration = {
        "logDriver" : "json-file", // /var/lib/docker/container
        "options" : {}
      }
    }
  ])
}

// Terraform strugles too much on destroying
resource "aws_ecs_service" "ecs_web_service" {
  name            = "e2m_web_service"
  cluster         = aws_ecs_cluster.containers_cluster.id
  task_definition = aws_ecs_task_definition.ecs_web_td.id
  desired_count   = 1

  network_configuration {
    security_groups = [var.private_web_sg_id]
    subnets         = var.public_subnet_ids
  }
  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.containers_cp.name
    weight            = 100
  }

  load_balancer {
    target_group_arn = var.lb_target_group_arn
    container_name = "cloud-ui"
    container_port = 80
  }

  provisioner "local-exec" {
    when = destroy
    command = <<EOF
    echo "Update to scale to 0"
    REGION=(echo ${self.cluster} | cut -d':' -f4)
    aws.exe ecs update-service --region REGION --cluster ${self.cluster} --service ${self.name} --desired-count 0 --force-new-deployment
    EOF
  }

  timeouts {
    delete = "10m"
  }

  depends_on = [var.auto_scaling_group_depends_on]
}

// ==========================================================
//                    Cloud-API service
// ==========================================================

resource "aws_ecs_task_definition" "ecs_api_td" {
  family             = "containers-api-task"
  network_mode       = "awsvpc"
  execution_role_arn = "arn:aws:iam::140023377333:role/ecsTaskExecutionRole"
  runtime_platform {
    operating_system_family = "LINUX"
    cpu_architecture        = "X86_64"
  }
  container_definitions = jsonencode([
    {
      name      = "cloud-api"
      image     = "140023377333.dkr.ecr.us-east-1.amazonaws.com/r2m-containers-repository:cloud-api"
      cpu       = 512
      memory    = 512
      essential = true
      portMappings = [ // Docker container not mapping ports
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
      healthCheck = {
        command = [
          "CMD-SHELL",
          "wget --spider http://localhost:8080/api/actuator/health || exit 1"
        ]
      }
      logConfiguration = {
        "logDriver" : "json-file", // /var/lib/docker/container
        "options" : {}
      }
      environment = [
        {
          name = "SPRING_PROFILES_ACTIVE"
          value = "local"
        }
      ]
    }
  ])
}

// Terraform strugles too much on destroying
resource "aws_ecs_service" "ecs_api_service" {
  name            = "e2m_api_service"
  cluster         = aws_ecs_cluster.containers_cluster.id
  task_definition = aws_ecs_task_definition.ecs_api_td.id
  desired_count   = 1

  # service_connect_configuration {
  #   enabled = true
  # }

  network_configuration {
    security_groups = [var.private_api_sg_id]
    subnets         = var.public_subnet_ids
  }

  # force_new_deployment = true
  # placement_constraints {
  #   type = "distinctInstance"
  # }

  # triggers = {
  #   redeployment = timestamp()
  # }

  capacity_provider_strategy {
    capacity_provider = aws_ecs_capacity_provider.containers_cp.name
    weight            = 100
  }

  service_registries {
    registry_arn = var.service_discovery_arn
    container_name = "cloud-api"
  }

  provisioner "local-exec" {
    when = destroy
    command = <<EOF
    echo "Update to scale to 0"
    REGION=(echo ${self.cluster} | cut -d':' -f4)
    aws.exe ecs update-service --region REGION --cluster ${self.cluster} --service ${self.name} --desired-count 0 --force-new-deployment
    EOF
  }

  timeouts {
    delete = "10m"
  }

  depends_on = [var.auto_scaling_group_depends_on]
}


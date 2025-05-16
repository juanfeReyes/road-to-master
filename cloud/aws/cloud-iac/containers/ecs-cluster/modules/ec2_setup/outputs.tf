
output "auto_scaling_group_arn" {
  description = "Auto Scaling group ARN"
  value = aws_autoscaling_group.ecs_asg.arn
}

output "auto_scaling_group" {
  description = "Auto Scaling group reference"
  value = aws_autoscaling_group.ecs_asg
}

output "private_web_sg_id" {
  description = "Public security group id"
  value = aws_security_group.private_web_sg.id
}

output "private_api_sg_id" {
  description = "Public security group id"
  value = aws_security_group.private_api_sg.id
}

output "lb_tg_arn" {
  description = "Load balancer target group arn"
  value = aws_lb_target_group.ecs_lb_target_group.arn
}

output "service_discovery_arn" {
  description = "Discovery service ARN"
  value = aws_service_discovery_service.containers_service_discovery_service.arn
}

variable "auto_scaling_group_depends_on" {
  type = any
  default = []
  description = "Auto scalling group reference to depend on"
}

variable "auto_scaling_group_arn" {
  type = string
  description = "Auto Scalling group ARN"
}

variable "private_web_sg_id" {
  type = string
  description = "Security group id for Web"
}

variable "private_api_sg_id" {
  type = string
  description = "Security group id for API"
}

variable "public_subnet_ids" {
  type = list(string)
  description = "Public subnet ids"
}

variable "private_subnet_ids" {
  type = list(string)
  description = "Private subnet ids"
}

variable "lb_target_group_arn" {
  type = string
  description = "Load balancer target group ARN"
}

variable "service_discovery_arn" {
  type = string
  description = "Service discovery ARN"
}


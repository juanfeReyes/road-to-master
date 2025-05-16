output "ecr_repository_url" {
  value = aws_ecr_repository.containers_repository.repository_url
  description = "ECR repository URL"
}
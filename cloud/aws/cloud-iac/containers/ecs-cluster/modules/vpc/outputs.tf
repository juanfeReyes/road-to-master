
output "vpc_id" {
  description = "Created VPC"
  value = aws_vpc.containers_vpc.id
}

output "public_subnet_ids" {
  description = "Public subnet created"
  value = [aws_subnet.public_subnet_a.id, aws_subnet.public_subnet_b.id]
}

output "private_subnet_ids" {
  description = "Private subnet created"
  value = [aws_subnet.private_subnet.id]
}

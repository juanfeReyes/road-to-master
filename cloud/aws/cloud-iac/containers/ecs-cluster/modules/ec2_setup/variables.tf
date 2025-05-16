
variable "vpc_id" {
  type = string
  description = "Id of the VPC"
}


variable "public_subnet_ids" {
  type = list(string)
  description = "Ids of the public subnet"
}
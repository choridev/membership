# Region
output "aws_region" {
  description = "Region of VPC"
  value       = var.aws_region
}

output "region_namespace" {
  description = "Region name without '-'"
  value       = replace(var.aws_region, "-", "")
}

# Availability zones
output "availability_zones" {
  description = "Availability zone list of VPC"
  value       = var.availability_zones
}

# VPC
output "vpc_name" {
  description = "The name of the VPC which is also the environment name"
  value       = var.vpc_name
}

output "vpc_id" {
  description = "VPC ID of newly created VPC"
  value       = aws_vpc.default.id
}

output "cidr_block" {
  description = "CIDR block of VPC"
  value       = aws_vpc.default.cidr_block
}

output "cidr_numeral" {
  description = "The number that specifies the vpc range (B class)"
  value       = var.cidr_numeral
}

output "internal_domain_name" {
  description = "Base domain name for internal"
  value       = var.internal_domain_name
}

# Shard
output "shard_id" {
  description = "The shard ID which will be used to distinguish the env of resources"
  value       = var.shard_id
}

output "shard_short_id" {
  description = "Shorter version of shard ID"
  value       = var.shard_short_id
}

# Public subnets
output "public_subnet_ids" {
  description = "List of public subnet ID in VPC"
  value       = aws_subnet.public.*.id
}

# Private subnets
output "private_subnet_ids" {
  description = "List of private subnet ID in VPC"
  value       = aws_subnet.private.*.id
}

# Private DB subnets
output "private_db_subnet_ids" {
  description = "List of DB private subnet ID in VPC"
  value       = aws_subnet.private_db.*.id
}

# Security group
output "aws_security_group_bastion_id" {
  description = "ID of bastion security group"
  value       = aws_security_group.bastion.id
}

output "aws_security_group_bastion_aware_id" {
  description = "ID of bastion aware security group"
  value       = aws_security_group.bastion_aware.id
}

output "aws_security_group_default_id" {
  description = "ID of default security group"
  value       = aws_security_group.default.id
}

output "aws_security_group_home_id" {
  description = "ID of home security group"
  value       = aws_security_group.home.id
}

# ETC
output "env_suffix" {
  description = "Suffix of the environment"
  value       = var.env_suffix
}

output "billing_tag" {
  description = "The environment value for biliing consolidation."
  value       = var.billing_tag
}

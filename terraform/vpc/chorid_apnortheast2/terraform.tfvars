aws_region = "ap-northeast-2"

# d after name indicates development
# This means that chorid_apnortheast2 VPC is for development environment VPC in Seoul region
vpc_name = "chorid_apnortheast2"

cidr_numeral = "10"

# Availability Zone list
availability_zones = ["ap-northeast-2a", "ap-northeast-2c"]

# In Seoul Region, some resources are not supported in ap-northeast-2b
availability_zones_without_b = ["ap-northeast-2a", "ap-northeast-2c"]

internal_domain_name = "chori.internal"

# shard_id will be used later when creating other resources
# With shard_id, you could distinguish which environment the resource belongs to 
shard_id       = "choridapne2"
shard_short_id = "chori01d"

# Billing tag in this VPC
billing_tag = "dev"

# d means develop
env_suffix = "d"

# Change here to your office or house
home_ips = {
  Chori = "221.149.143.136/32"
}

terraform {
  required_version = ">= 1.11.4"

  backend "s3" {
    bucket         = "chori-preprod-apnortheast2-tfstate"
    key            = "before-leaving-test/terraform/vpc/chorid_apnortheast2/terraform.tfstate"
    region         = "ap-northeast-2"
    encrypt        = true
    dynamodb_table = "terraform-lock"
  }
}

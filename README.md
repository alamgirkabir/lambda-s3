# Access s3 bucket from the lambda
This project is a sample to access s3 bucket from a lambda

If you need basic setup for lambda you can check my [repo](https://github.com/alamgirkabir/lambda)

To create a bucket in localstack

```bash
awslocal s3api create-bucket --bucket bucket-example
```

To check the list of buckets
```bash
awslocal s3api list-buckets
```

To create a new directory in the bucket you can use
```bash
awslocal s3api put-object --bucket bucket-example --key test
```

What next??
Will work on iam role, policy and terraform

## Resources
* All [cli commands](https://docs.aws.amazon.com/cli/latest/) in aws
* Important link for [localstack command](https://lobster1234.github.io/2017/04/05/working-with-localstack-command-line/)
* [Terraform](https://rahullokurte.com/serverless-event-scheduling-with-aws-eventbridge-and-lambda-using-terraform-and-localstack#heading-terraform-configuration) information for localstack
* [Terraform](https://www.youtube.com/watch?v=KgD3D02NDyQ) important video
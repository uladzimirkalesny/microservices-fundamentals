# Microservices Fundamentals 3

## resource-service:

### Module-1 - Microservice Architecture Overview

Prepare Program Arguments for AWS Configuration

```commandline
--access.key=AWS_ACCESS_KEY
--secret.key=AWS_SECRET_KEY
--region.name=REGION
```

### Module-2 - Microservices Communication

RabbitMQ Default Credentials:
```commandline
url      : http://localhost:15672/
username : guest
password : guest
```

Listing queues:
```commandline
Unix    : sudo rabbitmqctl list_queues
Windows : rabbitmqctl.bat list_queues
```

## resource-processor:

### Module-1 - Microservice Architecture Overview

Prepare Program Argument that represents path to read file from internal drive

```commandline
C:\Users\USER_NAME\RESOURCE.mp3
```
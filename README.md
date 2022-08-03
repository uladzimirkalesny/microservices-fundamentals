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

### Module-8 - Microservices Security
First of all need to add to /etc/hosts file following entry
```cmd
127.0.0.1 keycloak
```
Take a look fact that into docker-compose file should be define hostname and port for keycloak
```dockerfile
KEYCLOAK_HOSTNAME: keycloak
KEYCLOAK_HTTP_PORT: 8100
```
```dockerfile
commnad:
    - "-Djboss.http.port=8100"
```
```dockerfile
ports:
    - "8100:8100"
```
Start page url : http://keycloak:8100/auth/

Keycloak Properties:
```cmd
keycloak.realm: the name of the realm, required;
keycloak.resource: the client-id of the application, required;
keycloak.auth-server-url: the base URL of the Keycloak server, required;
keycloak.ssl-required: establishes if communications with the Keycloak server must happen over HTTPS. Here, it's set to external, meaning that it's only needed for external requests (default value). In production, instead, we should set it to all. Optional;
keycloak.credentials.secret: secret of service (client)
```
# kdb-load-test

## Build Setup

1. Check out this repository.

2. Add ".env" file and set environmental variables in it. Check .env.example for the list of variables to be set and/or use the section below.

3. Install Docker.

## Environment variables

Below is the list of recommended content for your local .env file.

KLT_MYSQL_ROOT_PASSWORD=root

KLT_MYSQL_PORT=3306

KLT_CORE_SERVER_PORT=9091

KLT_CORE_SQL_DATABASE=klt

KLT_CORE_SQL_USER=klt

KLT_CORE_SQL_PASSWORD=1234567890

KLT_CORE_SERVER_PORT=9091

KLT_CORE_DB_URL=jdbc:mysql://klt-db:3306/klt?useSSL=true&enabledTLSProtocols=TLSv1.2

KLT_GENERATE_MSISDN_RECORDS=2000000

## Docker

### Start the system

```bash
docker compose up -d
```

### Shutdown the system

```bash
docker compose down
```

### Rebuild an individual service

```bash
docker compose build klt-core
```

### Check the latest build date of a service

```bash
docker inspect -f '{{.Created}}' klt-core
```

### Redeploy an individual service

```bash
docker compose up --no-deps -d klt-core
```

### Connect to logs of Spring Boot backend

```bash
docker logs --tail 50 --follow --timestamps klt-core
```

## Testing

Maven shall be used for unit testing.

```bash
mvn test
```

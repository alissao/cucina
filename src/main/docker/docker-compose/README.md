This folder contains a Docker Compose configuration intended for local development when running Quarkus in dev mode.

File: `docker-compose.yaml`

Services included (pre-configured to match Quarkus DevServices defaults):
- otel-collector: OpenTelemetry Collector for tracing/metrics
- postgres: Postgres 15 exposed on 5432 (DB: cucina_db, user: cucina, password: cucina)
- zookeeper + kafka: Confluent images for Kafka on 9092
- keycloak: Keycloak dev mode on 8081
- redis: Redis on 6379
- vault: HashiCorp Vault dev mode on 8200 (root token: `root`)

Usage

1. Start all services and Quarkus in dev mode:

```bash
make run-dev
```

Or, to start only the services:

```bash
cd src/main/docker/docker-compose
docker compose up -d
```

2. Run Quarkus in dev mode (project root):

```bash
./gradlew quarkusDev
```

Notes

- The images and versions are chosen for local dev convenience. You can pin different versions as needed.
- If you already have these services running on the same ports, change the ports in the compose file or stop the local services first.
- For Vault and Keycloak in prod, avoid dev-mode options and configure secure storage and TLS.

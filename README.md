# Vaudoise-test-technique

## Docker Setup
To start PostgreSQL locally with Docker:
```bash
docker compose up -d
```

The database will be available at localhost:5432 with:
- user: postgres
- password: postgres
- database: vaudoise_api

Then simply run:
```bash
mvn spring-boot:run
```
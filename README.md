# clinica-medica

# Correr imagen de docker
docker run --name postgres-pweb \
-e POSTGRES_PASSWORD=camilo \
-e POSTGRES_USER=camilo \
-e POSTGRES_DB=clinica_db \
-p 15432:5432 -d postgres

# Crear Roles
INSERT into roles (name) VALUES ('USER'); \
INSERT INTO roles (name) values ('ADMIN');
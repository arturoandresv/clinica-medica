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

# Crear Admin
INSERT INTO users (username, email, password) \
VALUES ( 'admin@clinica.com', 'admin@clinica.com', '$2a$10$kVXIdRHi9wZqR1ntLzQ43OrhTWIivZqArVNO.f6ivltVxkzE7YHeW'); \
-- password: '123456'
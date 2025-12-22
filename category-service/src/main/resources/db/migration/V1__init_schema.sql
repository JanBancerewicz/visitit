CREATE TABLE client (
                        id UUID PRIMARY KEY NOT NULL,
                        first_name VARCHAR(255) NOT NULL,
                        last_name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        phone VARCHAR(50),
                        CONSTRAINT uk_client_email UNIQUE (email)
);

CREATE TABLE employee (
                          id UUID PRIMARY KEY NOT NULL,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          role VARCHAR(255) NOT NULL,
                          description TEXT
);

CREATE TABLE room (
                      id UUID PRIMARY KEY NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      CONSTRAINT uk_room_name UNIQUE (name)
);

CREATE TABLE service (
                         id UUID PRIMARY KEY NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         duration_min INTEGER NOT NULL,
                         price DECIMAL(10, 2) NOT NULL,
                         CONSTRAINT uk_service_name UNIQUE (name)
);
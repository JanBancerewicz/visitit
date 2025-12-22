-- Tabele referencyjne (kopie)
CREATE TABLE client_ref (
                            id UUID PRIMARY KEY NOT NULL,
                            display_name VARCHAR(255) NOT NULL
);

CREATE TABLE employee_ref (
                              id UUID PRIMARY KEY NOT NULL,
                              display_name VARCHAR(255) NOT NULL
);

CREATE TABLE room_ref (
                          id UUID PRIMARY KEY NOT NULL,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE service_ref (
                             id UUID PRIMARY KEY NOT NULL,
                             name VARCHAR(255) NOT NULL
);

-- Tabele główne
CREATE TABLE reservation (
                             id UUID PRIMARY KEY NOT NULL,
                             version BIGINT,
                             client_id UUID NOT NULL,
                             employee_id UUID NOT NULL,
                             service_id UUID NOT NULL,
                             room_id UUID NOT NULL,
                             start_datetime TIMESTAMP NOT NULL,
                             end_datetime TIMESTAMP NOT NULL,
                             status VARCHAR(50) NOT NULL,
                             note TEXT,
                             CONSTRAINT fk_res_client FOREIGN KEY (client_id) REFERENCES client_ref(id),
                             CONSTRAINT fk_res_employee FOREIGN KEY (employee_id) REFERENCES employee_ref(id),
                             CONSTRAINT fk_res_service FOREIGN KEY (service_id) REFERENCES service_ref(id),
                             CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES room_ref(id)
);

CREATE TABLE payment (
                         id UUID PRIMARY KEY NOT NULL,
                         reservation_id UUID NOT NULL,
                         amount DECIMAL(10, 2) NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         method VARCHAR(50) NOT NULL,
                         payment_date TIMESTAMP NOT NULL,
                         CONSTRAINT uk_payment_reservation UNIQUE (reservation_id),
                         CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(id)
);
CREATE TABLE "package"
(
    "id"               serial PRIMARY KEY,
    "price"            int NOT NULL,
    "number_of_months" int NOT NULL
);

INSERT INTO package (price, number_of_months) values (1100000, 1),(6300000,6),(10500000,12);
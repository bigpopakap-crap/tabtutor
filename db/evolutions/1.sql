# --- !Ups
CREATE TYPE pk_t AS INTEGER(20);
CREATE TYPE username_t AS VARCHAR(60);
CREATE TYPE name_t AS VARCHAR(60);
CREATE TYPE email_t AS VARCHAR(60);
CREATE TYPE fbid_t AS VARCHAR(60);

CREATE TABLE User (
	pk pk_t PRIMARY KEY,
	username username_t NOT NULL,
	firstName name_t NOT NULL,
	lastName name_t NOT NULL,
	email email_t NOT NULL,
	fbid fbid_t NOT NULL UNIQUE
);

CREATE TABLE Session (
	pk pk_t PRIMARY KEY,
	user_pk pk_t REFERENCES User(pk),
	UNIQUE(pk, user_pk)
);

# --- !Downs
DROP TYPE pk_t;
DROP TYPE username_t;
DROP TYPE name_t;
DROP TYPE email_t;
DROP TYPE fbid_t;
DROP TABLE Session;
DROP TABLE User;
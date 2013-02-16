# --- !Ups
CREATE TYPE pk_t AS INTEGER;
CREATE TYPE user_username_t AS VARCHAR(60);
CREATE TYPE user_name_t AS VARCHAR(60);
CREATE TYPE email_t AS VARCHAR(60);
CREATE TYPE facebook_id_t AS VARCHAR(60);

CREATE TABLE User (
	pk pk_t PRIMARY KEY,
	username user_username_t NOT NULL,
	fbid facebook_id_t NOT NULL UNIQUE,
	firstName user_name_t NOT NULL,
	lastName user_name_t NOT NULL,
	email email_t NOT NULL,
	registeredTime timestamp NOT NULL
);

CREATE TABLE Session (
	pk pk_t PRIMARY KEY,
	user_pk pk_t REFERENCES User(pk),
	startTime timestamp NOT NULL,
	updatedTime timestamp NOT NULL
	UNIQUE(pk, user_pk)
);

# --- !Downs
DROP TYPE pk_t;
DROP TYPE user_username_t;
DROP TYPE user_name_t;
DROP TYPE email_t;
DROP TYPE facebook_id_t;

DROP TABLE Session;
DROP TABLE User;
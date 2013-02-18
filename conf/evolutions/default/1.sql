# --- !Ups
CREATE TYPE pk_t AS UUID;
CREATE TYPE user_username_t AS VARCHAR(80);
CREATE TYPE user_name_t AS VARCHAR(80);
CREATE TYPE email_t AS VARCHAR(255);
CREATE TYPE facebook_id_t AS VARCHAR(80);
CREATE TYPE facebook_token_t AS VARCHAR(80);

CREATE TABLE User (
	pk pk_t PRIMARY KEY,
	username user_username_t NOT NULL UNIQUE,
	fbid facebook_id_t NOT NULL UNIQUE,
	fbIsAuthed BOOLEAN NOT NULL,
	firstName user_name_t NOT NULL,
	lastName user_name_t NOT NULL,
	email email_t NOT NULL,
	registerTime timestamp NOT NULL,
	lastLoginTime timestamp NOT NULL
);

CREATE TABLE Session (
	pk pk_t PRIMARY KEY,
	user_pk pk_t REFERENCES User(pk),
	fbtoken facebook_token_t,
	startTime timestamp NOT NULL,
	updateTime timestamp NOT NULL
);

# --- !Downs
DROP TYPE IF EXISTS pk_t;
DROP TYPE IF EXISTS user_username_t;
DROP TYPE IF EXISTS user_name_t;
DROP TYPE IF EXISTS email_t;
DROP TYPE IF EXISTS facebook_id_t;
DROP TYPE IF EXISTS facebook_token_t;

DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS User;
# --- !Ups
CREATE TYPE t_pk AS UUID;
CREATE TYPE t_userName AS VARCHAR(80);
CREATE TYPE t_email AS VARCHAR(255);
CREATE TYPE t_facebookId AS VARCHAR(80);
CREATE TYPE t_facebookToken AS VARCHAR(255);
CREATE TYPE t_csrfToken AS UUID;

CREATE TABLE User (
	pk t_pk PRIMARY KEY,
	fbId t_facebookId NOT NULL UNIQUE,
	fbIsAuthed BOOLEAN NOT NULL,
	firstName t_userName NOT NULL,
	lastName t_userName NOT NULL,
	email t_email NOT NULL,
	registerTime timestamp NOT NULL,
	lastLoginTime timestamp NOT NULL,
	secondToLastLoginTime timestamp,
	CHECK (lastLoginTime >= registerTime),
	CHECK ((secondToLastLoginTime IS NULL) OR (lastLoginTime >= secondToLastLoginTime))
);

CREATE TABLE Session (
	pk t_pk PRIMARY KEY,
	userPk t_pk REFERENCES User(pk),
	fbToken t_facebookToken,
	fbTokenExpireTime timestamp,
	startTime timestamp NOT NULL,
	lastAccessTime timestamp NOT NULL,
	CHECK ( -- both are null or both are not null
		(fbtoken IS NULL AND fbtokenExpireTime IS NULL)
		OR (fbtoken IS NOT NULL AND fbtokenExpireTime IS NOT NULL)
	),
	CHECK (lastAccessTime >= startTime)
);

CREATE TABLE SessionCsrfToken (
	sessionPk t_pk PRIMARY KEY,
	csrfToken t_csrfToken NOT NULL UNIQUE,
	createTime timestamp NOT NULL,
	expireTime timestamp NOT NULL,
	CHECK (expireTime >= createTime)
);

# --- !Downs
DROP TYPE IF EXISTS t_pk;
DROP TYPE IF EXISTS t_userName;
DROP TYPE IF EXISTS t_email;
DROP TYPE IF EXISTS t_facebookId;
DROP TYPE IF EXISTS t_facebookToken;
DROP TYPE IF EXISTS t_csrfToken;

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS SessionCsrfToken;
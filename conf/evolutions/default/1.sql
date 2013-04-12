# --- !Ups
CREATE TYPE t_pk AS UUID;
CREATE TYPE t_yearInt AS SMALLINT;
CREATE TYPE t_varcharLong AS VARCHAR(255);
CREATE TYPE t_varcharShort AS VARCHAR(80);
CREATE TYPE t_email AS t_varcharLong;
CREATE TYPE t_facebookId AS VARCHAR(80);
CREATE TYPE t_facebookToken AS t_varcharLong;
CREATE TYPE t_youtubeId AS VARCHAR(80);
CREATE TYPE t_csrfToken AS UUID;

CREATE TABLE Artist (
	pk t_pk PRIMARY KEY,
	name t_varcharLong NOT NULL
);

CREATE TABLE Album (
	pk t_pk PRIMARY KEY,
	title t_varcharLong NOT NULL,
	year t_yearInt,
	numTracks SMALLINT,
	artistPk t_pk REFERENCES Artist(pk)
);

CREATE TABLE NotationMeta (
	pk t_pk PRIMARY KEY,
	instrument t_varcharShort NOT NULL,
	skillLevel t_varcharShort NOT NULL,
	notationType t_varcharShort NOT NULL,
	ratingNumerator BIGINT NOT NULL,
	ratingDenomenator BIGINT NOT NULL,
	songPk t_pk NOT NULL REFERENCES Song(pk),
	userPk_author t_pk NOT NULL REFERENCES User(pk)
);

CREATE TABLE SessionCsrfToken (
	csrfToken t_csrfToken NOT NULL UNIQUE,
	createTime timestamp NOT NULL,
	expireTime timestamp NOT NULL,
	sessionPk t_pk NOT NULL REFERENCES Session(pk),
	CHECK (expireTime >= createTime)
);

CREATE TABLE Session (
	pk t_pk PRIMARY KEY,
	fbToken t_facebookToken,
	fbTokenExpireTime timestamp,
	startTime timestamp NOT NULL,
	lastAccessTime timestamp NOT NULL,
	userPk t_pk REFERENCES User(pk),
	CHECK ( -- both are null or both are not null
		(fbtoken IS NULL AND fbtokenExpireTime IS NULL)
		OR (fbtoken IS NOT NULL AND fbtokenExpireTime IS NOT NULL)
	),
	CHECK (lastAccessTime >= startTime)
);

CREATE TABLE Song (
	pk t_pk PRIMARY KEY,
	title t_varcharLong NOT NULL,
	trackNum SMALLINT,
	isLive BOOLEAN NOT NULL,
	youtubeId t_youtubeId,
	artistPk t_pk NOT NULL REFERENCES Artist(pk),
	albumPk t_pk REFERENCES Album(pk)
);

CREATE TABLE User (
	pk t_pk PRIMARY KEY,
	fbId t_facebookId NOT NULL UNIQUE,
	fbIsAuthed BOOLEAN NOT NULL,
	username t_userUsername NOT NULL UNIQUE,
	email t_email NOT NULL,
	registerTime timestamp NOT NULL,
	lastAccessTime timestamp NOT NULL,
	lastLoginTime timestamp NOT NULL,
	secondToLastLoginTime timestamp,
	CHECK (lastLoginTime >= registerTime),
	CHECK ((secondToLastLoginTime IS NULL) OR (lastLoginTime >= secondToLastLoginTime))
);

# --- !Downs
DROP TYPE IF EXISTS t_pk;
DROP TYPE IF EXISTS t_yearInt;
DROP TYPE IF EXISTS t_varcharLong;
DROP TYPE IF EXISTS t_varcharShort;
DROP TYPE IF EXISTS t_email;
DROP TYPE IF EXISTS t_facebookId;
DROP TYPE IF EXISTS t_facebookToken;
DROP TYPE IF EXISTS t_youtubeId;
DROP TYPE IF EXISTS t_csrfToken;

DROP TABLE IF EXISTS Album;
DROP TABLE IF EXISTS Artist;
DROP TABLE IF EXISTS NotationMeta;
DROP TABLE IF EXISTS SessionCsrfToken;
DROP TABLE IF EXISTS Session;
DROP TABLE IF EXISTS Song;
DROP TABLE IF EXISTS User;

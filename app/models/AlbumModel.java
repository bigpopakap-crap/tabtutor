package models;

import helpers.OperationReq;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.base.BaseModel;


/**
* This Ebean maps to the Album table, and represents album metadata
* 
* @author bigpopakap
* @since 2013-04-07
*
*/
@Entity
@Table(name = "Album")
public class AlbumModel extends BaseModel {
	
	private static final long serialVersionUID = 8389995907878983753L;
	
	private static final int INVALID_YEAR = -1;
	private static final int INVALID_NUM_TRACKS = -1;

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "title") public String title;
	@Column(name = "year") public int year;
	@Column(name = "numTracks") public int numTracks;
	
	@ManyToOne @JoinColumn(name = "artistPk", referencedColumnName = "pk") public ArtistModel artist;
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "albumPk", referencedColumnName = "pk") public Set<SongModel> songs; //TODO use ordered list?
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getTitle() { return title; }
	public boolean hasArtist() { return getArtist() != null; }
	public ArtistModel getArtist() { return artist; }
	public int getYear() { return year; }
	public int getNumTracks() { return numTracks; }
	public Set<SongModel> getSongs() { return songs; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, AlbumModel> FINDER = new Finder<>(
		UUID.class, AlbumModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */

	private AlbumModel(String title, ArtistModel artist, int year, int numTracks) {
		this.pk = UUID.randomUUID();
		this.title = title;
		this.artist = artist;
		this.year = year;
		this.numTracks = numTracks;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	public static AlbumModel createAndSave(String title) {
		return createAndSave(title, null);
	}
	
	public static AlbumModel createAndSave(String title, ArtistModel artist) {
		return createAndSave(title, artist, INVALID_YEAR, INVALID_NUM_TRACKS);
	}
	
	public static AlbumModel createAndSave(String title, ArtistModel artist, int year, int numTracks) {
		OperationReq.requireAndThrow(
			OperationReq.IS_LOGGED_IN
		);
		return (AlbumModel) new AlbumModel(title, artist, year, numTracks).doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets a Album by ID, converts the string to a UUID internally */
	public static AlbumModel getByPk(String pk) {
		try {
			return getByPk(pk != null ? UUID.fromString(pk) : null);
		}
		catch (IllegalArgumentException ex) {
			//the string was not a valid UUID
			return null;
		}
	}
	
	/** Gets a Album by ID */
	public static AlbumModel getByPk(UUID pk) {
		return pk != null ? FINDER.byId(pk) : null;
	}
	
	/** Get all albums */
	public static List<AlbumModel> getAll() {
		return FINDER.all();
	}
	
	/* **************************************************************************
	 * BEGIN  UPDATERS
	 ************************************************************************** */
	
	//TODO
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	//TODO

}

package models;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.base.BaseModel;

import com.avaje.ebean.annotation.Formula;

/**
* This Ebean maps to the Song table, and represents song metadata
* 
* @author bigpopakap
* @since 2013-04-07
*
*/
@Entity
@Table(name = "Song")
public class SongModel extends BaseModel {
	
	private static final long serialVersionUID = -7421630820257999626L;
	
	private static final int INVALID_TRACK_NUM = -1;

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	//TODO add a field for the year?
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "title") public String title;
	@Column(name = "trackNum") public int trackNum;
	@Column(name = "isLive") public boolean isLive;
	@Column(name = "youtubeId") public String youtubeId;
	
	@ManyToOne @JoinColumn(name = "artistPk", referencedColumnName = "pk") public ArtistModel artist;
	@ManyToOne @JoinColumn(name = "albumPk", referencedColumnName = "pk") public AlbumModel album;
	@OneToMany @JoinColumn(name = "songPk", referencedColumnName = "pk") public Set<NotationMetaModel> notations; //TODO use ordered list?
	
	@Transient @Formula(select = "(youtubeId IS NOT NULL)") public boolean isYoutubeEnabled;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getTitle() { return title; }
	public ArtistModel getArtist() { return artist; }
	public boolean hasAlbum() { return getAlbum() != null; }
	public AlbumModel getAlbum() { return album; }
	public int getTrackNum() { return trackNum; }
	public boolean isLive() { return isLive; }
	public String getYoutubeId() { return youtubeId; }
	public boolean isYoutubeEnabled() { return isYoutubeEnabled; }
	public Set<NotationMetaModel> getNotations() { return notations; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, SongModel> FINDER = new Finder<>(
		UUID.class, SongModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */

	private SongModel(String title, ArtistModel artist, AlbumModel album, int trackNum, boolean isLive, String youtubeId) {
		this.pk = UUID.randomUUID();
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.trackNum = trackNum;
		this.isLive = isLive;
		this.youtubeId = youtubeId;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	public static SongModel createAndSave(String title) {
		return createAndSave(title, null);
	}
	
	public static SongModel createAndSave(String title, ArtistModel artist) {
		return createAndSave(title, artist, null);
	}
	
	public static SongModel createAndSave(String title, ArtistModel artist, AlbumModel album) {
		return createAndSave(title, artist, album, INVALID_TRACK_NUM, false);
	}
	
	public static SongModel createAndSave(String title, ArtistModel artist, AlbumModel album, int trackNum, boolean isLive) {
		return createAndSave(title, artist, album, trackNum, isLive, null);
	}
	
	public static SongModel createAndSave(String title, ArtistModel artist, AlbumModel album, int trackNum, boolean isLive, String youtubeId) {
		return (SongModel) new SongModel(title, artist, album, trackNum, isLive, youtubeId).doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets a Song by ID, converts the string to a UUID internally */
	public static SongModel getByPk(String pk) {
		try {
			return getByPk(pk != null ? UUID.fromString(pk) : null);
		}
		catch (IllegalArgumentException ex) {
			//the string was not a valid UUID
			return null;
		}
	}
	
	/** Gets a Song by ID */
	public static SongModel getByPk(UUID pk) {
		return pk != null ? FINDER.byId(pk) : null;
	}
	
	/** Get all songs */
	public static List<SongModel> getAll() {
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

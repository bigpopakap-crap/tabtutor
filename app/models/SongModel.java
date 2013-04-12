package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "title") public String title;
	@Column(name = "trackNum") public int trackNum;
	@Column(name = "isLive") public boolean isLive;
	@Column(name = "youtubeId") public String youtubeId;
	//TODO use proper foreign object reference for StaffMetaModel list
	
	@OneToOne @JoinColumn(name = "artistPk") public ArtistModel artist;
	@OneToOne @JoinColumn(name = "albumPk") public AlbumModel album;
	
	@Transient @Formula(select = "(youtubeId IS NOT NULL)") public boolean isYoutubeEnabled;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getTitle() { return title; }
	public ArtistModel getArtist() { return artist; }
	public AlbumModel getAlbum() { return album; }
	public int getTrackNum() { return trackNum; }
	public boolean isLive() { return isLive; }
	public String getYoutubeId() { return youtubeId; }
	public boolean isYoutubeEnabled() { return isYoutubeEnabled; }
	
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

	//TODO
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	//TODO
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	//TODO
	
	/* **************************************************************************
	 * BEGIN  UPDATERS
	 ************************************************************************** */
	
	//TODO
	
	/* **************************************************************************
	 *  BEGIN VALIDATORS
	 ************************************************************************** */
		
	//TODO
	
}

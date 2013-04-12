package models;

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

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "title") public String title;
	@Column(name = "year") public int year;
	@Column(name = "numTracks") public int numTracks;
	
	@ManyToOne @JoinColumn(name = "artistPk", referencedColumnName = "pk") public ArtistModel artist;
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "albumPk", referencedColumnName = "pk") public Set<SongModel> songs;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getTitle() { return title; }
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

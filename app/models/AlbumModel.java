package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	@Column(name = "artistPk") public UUID artistPk; //TODO use proper foreign object reference
	@Column(name = "year") public int year;
	@Column(name = "numTracks") public int numTracks;
	//TODO use proper foreign object reference for song list
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getTitle() { return title; }
	public UUID getArtistPk() { return UUID.fromString(artistPk.toString()); } //defensive copy
	public int getYear() { return year; }
	public int getNumTracks() { return numTracks; }
	
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

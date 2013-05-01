package models;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* This Ebean maps to the Artist table, and represents artist metadata
* 
* @author bigpopakap
* @since 2013-04-07
*
*/
@Entity
@Table(name = "Artist")
public class ArtistModel extends BaseModel {
	
	private static final long serialVersionUID = 1905031394525454063L;

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "name") public String name;
	
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "artistPk", referencedColumnName = "pk") public Set<AlbumModel> albums; //TODO use ordered list?
	@OneToMany(fetch = FetchType.LAZY) @JoinColumn(name = "artistPk", referencedColumnName = "pk") public Set<SongModel> songs; //TODO use ordered list?
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getName() { return name; }
	public Set<AlbumModel> getAlbums() { return albums; }
	public Set<SongModel> getSongs() { return songs; }
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, ArtistModel> FINDER = new Finder<>(
		UUID.class, ArtistModel.class
	);
	
	/* **************************************************************************
	 *  BEGIN HOOKS
	 ************************************************************************** */
	
	/* **************************************************************************
	 *  BEGIN CONSTRUCTORS (PRIVATE)
	 ************************************************************************** */

	private ArtistModel(String name) {
		this.name = name;
	}
	
	/* **************************************************************************
	 *  BEGIN CREATORS (PUBLIC)
	 ************************************************************************** */
	
	public static ArtistModel createAndSave(String name) {
		return (ArtistModel) new ArtistModel(name).doSaveAndRetry();
	}
	
	/* **************************************************************************
	 *  BEGIN SELECTORS
	 ************************************************************************** */
	
	/** Gets a Artist by ID, converts the string to a UUID internally */
	public static ArtistModel getByPk(String pk) {
		try {
			return getByPk(pk != null ? UUID.fromString(pk) : null);
		}
		catch (IllegalArgumentException ex) {
			//the string was not a valid UUID
			return null;
		}
	}
	
	/** Gets a Artist by ID */
	public static ArtistModel getByPk(UUID pk) {
		return pk != null ? FINDER.byId(pk) : null;
	}
	
	/** Get all artists */
	public static List<ArtistModel> getAll() {
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

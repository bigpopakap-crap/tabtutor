package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	//TODO use proper foreign object reference for album list
	//TODO use proper foreign object reference for song list
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public String getName() { return name; }
	
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

package models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
* This Ebean maps to the Staff table, and represents staff metadata
* A "staff" is the notation for a particular instrument
* 
* @author bigpopakap
* @since 2013-04-07
*
*/
@Entity
@Table(name = "StaffMeta")
public class StaffMetaModel extends BaseModel {

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	//TODO
	//TODO use proper foreign object reference for song
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, StaffMetaModel> FINDER = new Finder<>(
		UUID.class, StaffMetaModel.class
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

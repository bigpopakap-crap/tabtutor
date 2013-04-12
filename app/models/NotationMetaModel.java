package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import types.objects.InstrumentType;
import types.objects.NotationType;
import types.objects.SkillLevelType;

/**
* This Ebean maps to the Staff table, and represents staff metadata
* A "staff" is the notation for a particular instrument
* 
* @author bigpopakap
* @since 2013-04-07
*
*/
@Entity
@Table(name = "NotationMeta")
public class NotationMetaModel extends BaseModel {
	
	private static final long serialVersionUID = 4536159940032586861L;

	/* **************************************************************************
	 *  FIELDS
	 ************************************************************************** */
	
	@Column(name = "pk") @Id public UUID pk;
	@Column(name = "songPk") public UUID songPk; //TODO use proper foreign object reference
	@Column(name = "userPk_author") public UUID userPk_author; //TODO use proper foreign object reference
	@Column(name = "instrumentType") public InstrumentType instrumentType;
	@Column(name = "skillLevelType") public SkillLevelType skillLevelType;
	@Column(name = "notationType") public NotationType notationType;
	@Column(name = "rating") public double rating;
	//TODO add reference to the actual notation data
	//TODO use proper foreign object reference for instrument type list
	
	/** Private helper for DB interaction implementation */
	private static final Finder<UUID, NotationMetaModel> FINDER = new Finder<>(
		UUID.class, NotationMetaModel.class
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

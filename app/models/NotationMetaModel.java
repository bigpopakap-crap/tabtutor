package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import models.base.BaseModel;

import types.musical.Instrument;
import types.musical.NotationType;
import types.musical.SkillLevel;

import com.avaje.ebean.annotation.Formula;

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
	@Column(name = "instrument") public Instrument instrument;
	@Column(name = "skillLevel") public SkillLevel skillLevel;
	@Column(name = "notationType") public NotationType notationType;
	@Column(name = "ratingNumerator") public int ratingNumerator;
	@Column(name = "ratingDenomenator") public int ratingDenomenator;
	//TODO add reference to the actual notation data
	//TODO use proper foreign object reference for instrument type list
	
	@ManyToOne @JoinColumn(name = "songPk", referencedColumnName = "pk") public SongModel song;
	@ManyToOne @JoinColumn(name = "userPk_author", referencedColumnName = "pk") public UserModel author;
	
	@Transient @Formula(select = "(CASE WHEN ratingDenomenator = 0 THEN 0 ELSE (CAST(ratingNumerator AS NUMERIC) / ratingDenomenator))") public double rating;
	
	public UUID getPk() { return UUID.fromString(pk.toString()); } //defensive copy
	public Instrument getInstrument() { return instrument; }
	public SkillLevel getSkillLevel() { return skillLevel; }
	public NotationType getNotationType() { return notationType; }
	public SongModel getSong() { return song; }
	public UserModel getAuthor() { return author; }
	public double getRating() { return rating; }
	
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

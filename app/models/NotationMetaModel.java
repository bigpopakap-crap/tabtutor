package models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import types.objects.InstrumentType;
import types.objects.NotationType;
import types.objects.SkillLevelType;

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
	@Column(name = "instrumentType") public InstrumentType instrumentType;
	@Column(name = "skillLevelType") public SkillLevelType skillLevelType;
	@Column(name = "notationType") public NotationType notationType;
	@Column(name = "ratingNumerator") public int ratingNumerator;
	@Column(name = "ratingDenomenator") public int ratingDenomenator;
	//TODO add reference to the actual notation data
	//TODO use proper foreign object reference for instrument type list
	
	@OneToOne @JoinColumn(name = "songPk") public SongModel song;
	@OneToOne @JoinColumn(name = "userPk_author") public UserModel user_author;
	
	@Transient @Formula(select = "(CASE WHEN ratingDenomenator = 0 THEN 0 ELSE (CAST(ratingNumerator AS NUMERIC) / ratingDenomenator))") public double rating;
	
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

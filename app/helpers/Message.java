package helpers;

import helpers.Universe.UniverseElement;
import play.i18n.Lang;
import play.i18n.Messages;
import contexts.SessionContext;

/**
 * This class holds all strings that will be internationalized
 * 
 * @author bigpopakap
 * @since 2013-03-07
 *
 */
public abstract class Message {
	
	//TODO test that no other class refers to the Messages class directly, except this class
	//TODO define debug language that adds a bunch of stars before and after the string
	
	/** The universe used to ensure that message keys are unique. This must be declared before the other values */
	private static final Universe<String> MESSAGE_KEY_UNIVERSE = new Universe<>();
	
	/* **************************************************************************
	 *  STATIC MESSAGE DECLARATIONS
	 ************************************************************************** */
	
	public static final Message word_user = new Message("word_user") {};
	public static final Message word_sign_in = new Message("word_sign_in") {};
	
	public static final Message formError_required = new Message("formError_required") {};
	public static final Message formError_notUnique = new Message("formError_notUnique") {};
	public static final Message formError_notInteger = new Message("formError_notInteger") {};
	
	public static final Message formInput_submit_label = new Message("formInput_submit_label") {};
	
	public static final Message formInput_username_label = new Message("formInput_username_label") {};
	public static final Message formInput_username_placeholder = new Message("formInput_username_placeholder") {};
	public static final Message formInput_username_helpText = new Message("formInput_username_helpText") {};
	
	public static final Message formInput_email_label = new Message("formInput_email_label") {};
	public static final Message formInput_email_placeholder = new Message("formInput_email_placeholder") {};
	public static final Message formInput_email_helpText = new Message("formInput_email_helpText") {};
	
	public static final Message formInput_songTitle_label = new Message("formInput_songTitle_label") {};
	public static final Message formInput_songTitle_placeholder = new Message("formInput_songTitle_placeholder") {};
	public static final Message formInput_songTitle_helpText = new Message("formInput_songTitle_helpText") {};
	
	public static final Message formInput_artistName_label = new Message("formInput_artistName_label") {};
	public static final Message formInput_artistName_placeholder = new Message("formInput_artistName_placeholder") {};
	public static final Message formInput_artistName_helpText = new Message("formInput_artistName_helpText") {};
	
	public static final Message formInput_albumTitle_label = new Message("formInput_albumTitle_label") {};
	public static final Message formInput_albumTitle_placeholder = new Message("formInput_albumTitle_placeholder") {};
	public static final Message formInput_albumTitle_helpText = new Message("formInput_albumTitle_helpText") {};
	
	public static final Message errorPage_title = new Message("errorPage_title") {};
	public static final Message errorPage_sampleDescription = new Message("errorPage_sampleDescription") {};
	public static final Message errorPage_internalServerErrorDescription = new Message("errorPage_internalServerErrorDescription") {};
	public static final Message errorPage_pageNotFoundDescription = new Message("errorPage_pageNotFoundDescription") {};
	public static final Message errorPage_csrfTokenInvalidDescription = new Message("errorPage_csrfTokenInvalidDescription") {};
	public static final Message errorPage_notAuthedDescription = new Message("errorPage_notAuthedDescription") {};
	public static final Message errorPage_insufficientPrivilegesDescription = new Message("errorPage_insufficientPrivilegesDescription") {};
	public static final Message errorPage_toGoBack = new Message("errorPage_toGoBack") {};
	public static final Message errorPage_toGoHome = new Message("errorPage_toGoHome") {};
	public static final Message errorPage_toSignIn = new Message("errorPage_toSignIn") {};
	
	/* **************************************************************************
	 *  CLASS DEFINITION
	 ************************************************************************** */
	
	/** The key used to access the string in the messages files */
	private final UniverseElement<String> key;
	
	/** Creates a new message with the given lookup key */
	private Message(String key) {
		if (key == null) throw new IllegalArgumentException("key cannot be null");
		this.key = MESSAGE_KEY_UNIVERSE.register(key);
		
		//ensure that calling get actually returns something
		String val = get();
		if (val == null) {
			throw new IllegalStateException("Value returned by get() is null for message " + MESSAGE_KEY_UNIVERSE.extract(this.key));
		}
		else if (val.isEmpty()) {
			Logger.warn("Value returned by get() is empty for message " + MESSAGE_KEY_UNIVERSE.extract(this.key));
		}
	}
	
	/** 
	 * This toString() throws an exception. Callers should use the get() methods to
	 * get the translation
	 * 
	 * @see #get(Object...)
	 * @see #get(Lang, Object...) */
	@Override
	public String toString() {
		throw new UnsupportedOperationException(
			"Should not call toString on a message. Use the get() methods instead"
		);
	}
	
	/** Gets the translation of this message in the language of the context */
	public String get(Object... params) {
		return get(SessionContext.lang(), params);
	}
	
	/** Gets the translation of this message in the given language */
	public String get(Lang lang, Object... params) {
		return Messages.get(lang, MESSAGE_KEY_UNIVERSE.extract(key), params);
	}
	
}

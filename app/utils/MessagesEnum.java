package utils;

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
public enum MessagesEnum {
	
	//TODO test that no other class refers to the Messages class directly, except this class
	//TODO test that no message have duplicate keys
	//TODO test that each message in this enum actually gets a string with the get() method
	
	//TODO define debug language that adds a bunch of stars before and after the string
	
	word_user("word_user"),
	formError_required("formError_required"),
	formError_notUnique("formError_notUnique"),
	formError_notInteger("formError_notInteger"),
	word_login("word_login"),
	errorPage_title("errorPage_title"),
	errorPage_internalServerErrorDescription("errorPage_internalServerErrorDescription"),
	errorPage_pageNotFoundDescription("errorPage_pageNotFoundDescription"),
	errorPage_toGoBack("errorPage_toGoBack"),
	errorPage_toGoHome("errorPage_toGoHome"),
	errorPage_sampleDescription("errorPage_sampleDescription"),
	errorPage_csrfTokenInvalid("errorPage_csrfTokenInvalid");
	
	/** The key used to access the string in the messages files */
	private final String key;
	
	/** Creates a new MessagesEnum item with the given key */
	private MessagesEnum(String key) {
		if (key == null) throw new IllegalArgumentException("Key cannot be null");
		this.key = key;
	}
	
	/** This toString() throws an exception. Callers should use the get() methods to
	 *  get the translation
	 *  @see #get(Object...)
	 *  @see #get(Lang, Object...) */
	@Override
	public String toString() {
		throw new UnsupportedOperationException(
			"Should not call toString on a message. Use the get() methods instead"
		);
	}
	
	/** Gets the key for this message, for whatever it's worth */
	public String key() {
		return key;
	}

	/** Gets the translation of this message in the language of the context */
	public String get(Object... params) {
		return get(SessionContext.lang(), params);
	}
	
	/** Gets the translation of this message in the given language */
	public String get(Lang lang, Object... params) {
		return Messages.get(lang, key(), params);
	}
	
}

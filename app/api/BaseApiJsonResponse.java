package api;

import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;

import play.libs.WS.Response;
import types.HttpMethodType;
import api.exceptions.BaseApiException;

/**
 * Base response object that expect the raw response to be in JSON format
 * Provides helper methods for getting information from the JSON response
 * 
 * @author bigpopakap
 * @since 2013-03-17
 * 
 * @param <T> the type of the exception that is associated with an erroneous response
 */
public abstract class BaseApiJsonResponse<T extends BaseApiException> extends BaseApiResponse<T> {
	
	private final JsonNode json; //the actual JSON response

	protected BaseApiJsonResponse(HttpMethodType method, String urlDomain, String urlPath, Map<String, String> params, Response resp) {
		super(method, urlDomain, urlPath, params, resp);
		this.json = resp.asJson();
	}
	
	/** Gets the JSON response itself */
	public JsonNode getJson() {
		return json;
	}
	
	/** Helper to find a value from the JSON if it originated from one of the
	 *  given API paths, or throw an error if it did not come from one of those paths */
	protected JsonNode find(String jsonPath, String... acceptableUrlPaths) {
		if (!is(acceptableUrlPaths)) {
			throw new UnsupportedOperationException("Getting this field is not supported for these API url paths");
		}
		else if (isError()) {
			//don't throw an error here, just return no data
			//callers *should* be checking for errors before this anyways
			return JsonNodeFactory.instance.nullNode();
		}
		else {
			return getJson().path(jsonPath);
		}
	}
	
}

package types;

/**
 * Encapsulates supported static resource types
 * 
 * Note: for now, we only support JS and CSS files
 * 
 * @author bigpopakap
 * @since 2013-03-17
 *
 */
public enum StaticResourceType {
	JS(".js"),
	CSS(".css");
	
	/** The file extension associated with that resource type */
	private final String fileExt;
	
	/** Creates a new object with the given associated file extension */
	private StaticResourceType(String fileExt) {
		if (fileExt == null) throw new IllegalArgumentException("fileExt cannot be null");
		this.fileExt = fileExt;
	}
	
	/** Determines if the path is for this static resource type */
	public boolean isType(String path) {
		return path != null && path.endsWith(fileExt);
	}
	
	/** Gets the type of the given path, or null if it doesn't match any types */
	public static StaticResourceType getTypeOf(String path) {
		for (StaticResourceType type : values()) {
			if (type.isType(path)) return type;
		}
		return null;
	}
	
}

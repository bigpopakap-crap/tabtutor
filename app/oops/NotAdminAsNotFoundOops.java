package oops;

/**
 * A particular case of the {@link NotFoundOops} that was caused by the user
 * not being an admin user
 * 
 * Note that while the cause is more like a {@link InsufficientPrivilegesOops},
 * it is parented by the {@link NotFoundOops} because it should be handled as such
 * 
 * The counterpart to this is the {@link NotAdminAsInsufficientPrivilegesOops}
 * 
 * @author bigpopakap
 * @since 2013-05-03
 *
 */
public class NotAdminAsNotFoundOops extends NotFoundOops {

	private static final long serialVersionUID = 6984449502391759433L;

	public NotAdminAsNotFoundOops(Throwable cause) {
		super(cause);
	}

}

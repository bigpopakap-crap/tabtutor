package oops;

/**
 * A particular case of the {@link InsufficientPrivilegesOops} that was caused by the user
 * not being an admin user
 * 
 * The counterpart to this is the {@link NotAdminAsNotFoundOops}, which has the same cause
 * (a user not being an admin), but is surfaced as a {@link NotFoundOops}
 * 
 * @author bigpopakap
 * @since 2013-05-03
 *
 */
public class NotAdminAsInsufficientPrivilegesOops extends InsufficientPrivilegesOops {

	private static final long serialVersionUID = 7687165422019182716L;

	public NotAdminAsInsufficientPrivilegesOops(Throwable cause) {
		super(cause);
	}

}

package helpers;

/**
 * Framework for operations that depend on each other. Each one
 * can must implement a method that actually does an operation
 * 
 * Furthermore, they can specify dependencies in the constructor
 * which will be run before this operation
 * 
 * @author bigpopakap
 * @since 2013-05-02
 *
 * @param <I> the input type of the operators
 * @param <O> the output type of the operators
 */
public abstract class DependentOperation<I, O> {
	
	/** The list of dependencies associated with this operation */
	private final DependentOperation<I, O>[] dependencies;
	
	/** Create a new object with the given dependencies */
	@SuppressWarnings("unchecked")
	public DependentOperation(DependentOperation<I, O>... dependencies) {
		this.dependencies = dependencies != null ? dependencies : (DependentOperation<I, O>[]) new DependentOperation[0];
	}
	
	/**
	 * Does the dependencies and the operation on the given input
	 * @see #hook_preDependenciesOperate(Object)
	 * @see #hook_postDependenciesOperate(Object)
	 */
	protected final O operate(I input) {
		//call the dependencies
		for (DependentOperation<I, O> op : dependencies) {
			O depOutput = op.operate(input);
			if (depOutput != null) {
				return depOutput;
			}
		}
		
		//call the post dependency operation
		return hook_postDependenciesOperate(input);
	}
	
	/**
	 * Called after the dependencies are called (if all the dependencies
	 * returned null). This will be the final return value of the operation
	 * if it is indeed called
	 */
	protected abstract O hook_postDependenciesOperate(I input);

}

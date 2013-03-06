package types;

/**
 * This interface holds two enums of SQL commands, one for modifying commands
 * and one for all commands (including non-modifying ones)
 * 
 * Both enums implement this interface for ease of converting between the two types
 * The interface allows for accepting any type of command, while using a particular
 * enum will be useful to limit the scope of which commands will be accepted
 * 
 * @author bigpopakap@gmail.com
 * @since 2013-03-05
 *
 */
public interface SqlCommandType {
	
	/** Returns true if this command type is one that can modify database data */
	public boolean isModifying();
	
	/** Returns the non-modifying version of this command type */
	public BasicDmlType getNonModifying();
	
	/** Returns the modifying version of this command type
	 *  Returns null if this has no modifying equivalent */
	public BasicDmlModifyingType getModifying();
	
	/**
	 * Enum of all SQL commands, including non-modifying ones
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-05
	 *
	 */
	public static enum BasicDmlType implements SqlCommandType {
		SELECT,
		INSERT(BasicDmlModifyingType.INSERT),
		UPDATE(BasicDmlModifyingType.UPDATE),
		DELETE(BasicDmlModifyingType.DELETE);
		
		/** The equivalent modifying command, or null if there is not an equivalent */
		private BasicDmlModifyingType equivalent;
		
		private BasicDmlType() {
			this(null);
		}
		
		private BasicDmlType(BasicDmlModifyingType equivalent) {
			this.equivalent = equivalent;
		}

		@Override
		public boolean isModifying() {
			return equivalent != null;
		}

		@Override
		public BasicDmlType getNonModifying() {
			return this;
		}

		@Override
		public BasicDmlModifyingType getModifying() {
			return equivalent;
		}
		
	}
	
	/**
	 * Enum of only modifying SQL commands
	 * 
	 * @author bigpopakap@gmail.com
	 * @since 2013-03-05
	 *
	 */
	public static enum BasicDmlModifyingType implements SqlCommandType {
		INSERT(BasicDmlType.INSERT),
		UPDATE(BasicDmlType.UPDATE),
		DELETE(BasicDmlType.DELETE);
		
		/** The equivalent non-modifying command, or null if there is not an equivalent
		 *  (though all of these do have a non-modifying equivalent) */
		private BasicDmlType equivalent;
		
		private BasicDmlModifyingType() {
			this(null);
		}
		
		private BasicDmlModifyingType(BasicDmlType equivalent) {
			this.equivalent = equivalent;
		}

		@Override
		public boolean isModifying() {
			return true;
		}

		@Override
		public BasicDmlType getNonModifying() {
			return equivalent;
		}

		@Override
		public BasicDmlModifyingType getModifying() {
			return this;
		}
		
	}

}

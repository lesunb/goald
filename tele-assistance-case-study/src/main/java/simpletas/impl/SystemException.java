package simpletas.impl;

public class SystemException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8239951327800470384L;
	
	public String serviceName;
	
	public SystemException(String serviceName) {
		this.serviceName = serviceName;
	}

}

package goald.behaviour;

public class CallFailure extends Exception {

	String identification;
	public CallFailure(String identification) {
		this.identification = identification;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1268468679818558743L;

}

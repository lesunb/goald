package goald.model;

public class Change {
	
	public enum Effect  {
		UNKNOWN,
		NEUTRAL,
		FAILURE
	};
	
	protected Long time;

	private Effect effect = Effect.UNKNOWN;
	
	public Long getTime() {
		return this.time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Effect getEffect() {
		return effect;
	}

	public void setEffect(Effect effect) {
		this.effect = effect;
	}
}
package goald.behaviour;

public interface RandomFailureTrower {

	void call(Float probl, String identification) throws CallFailure;

}

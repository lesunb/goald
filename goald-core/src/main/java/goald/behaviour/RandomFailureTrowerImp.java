package goald.behaviour;

public class RandomFailureTrowerImp implements RandomFailureTrower {

	public RandomFailureTrowerImp() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void call(Float probl, String identification) throws CallFailure {
		if(Math.random() < probl) {
			throw new CallFailure(identification);
		}
	}

}

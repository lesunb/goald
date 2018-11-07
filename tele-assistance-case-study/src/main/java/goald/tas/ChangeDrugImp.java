package goald.tas;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.tas.definitions.ChangeDrug;

public class ChangeDrugImp implements ChangeDrug {

	@Inject
	Logger log;
	
	@Override
	public void exec() {
		log.debug("changing drug");
	}
	
}

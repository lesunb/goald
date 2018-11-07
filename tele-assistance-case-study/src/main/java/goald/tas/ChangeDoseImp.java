package goald.tas;

import javax.inject.Inject;

import org.slf4j.Logger;

import goald.tas.definitions.ChangeDose;

public class ChangeDoseImp implements ChangeDose {

	
	@Inject
	Logger log;
	
	@Override
	public void exec() {
		log.debug("changing dose");
	}

}

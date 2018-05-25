package goald.dam.model;

import java.util.ArrayList;
import java.util.List;

public class DeploymentPlan {

	public enum DeployOp {
		INSTALL,
		RESOLVE,
		UPDATE,
		REFRESH,
		START,
		STOP,
		UNINSTALL
	}
	
	private List<Command> commands;
	
	public class Command {
		private DeployOp op;
		private Bundle bundle;
		
		public Command(DeployOp op, Bundle bundle) {
			this.op = op;
			this.bundle = bundle;
		}
		
		public DeployOp getOp() {
			return this.op;
		}
		
		public Bundle getBundle() {
			return this.bundle;
		}
	}
	


	public List<Command> getCommands() {
		if(commands == null) {
			commands = new ArrayList<>();
		}
		return commands;
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}
}

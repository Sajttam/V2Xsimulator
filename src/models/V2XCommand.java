package models;

import java.io.Serializable;

public class V2XCommand implements Serializable {
	public enum Commands {
		STOP, DRIVE;
	}
	
	private Commands command;
	
	public V2XCommand () {
		setCommand(Commands.STOP);
	}
	
	public V2XCommand (Commands command) {
		setCommand(command);
	}
	
	public Commands getCommand() {
		return command;
	}

	public void setCommand(Commands command) {
		this.command = command;
	}
}

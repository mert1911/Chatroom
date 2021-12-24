package message.impl;

import message.Message;
import message.MessageType;

//TODO Exercise (a)
public class LogoutMessage extends Message {

	private static final long serialVersionUID = 1L;

	public LogoutMessage() {
		super(MessageType.LOGOUT);
	}

}

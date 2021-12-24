package message.impl;

import message.Message;
import message.MessageType;

//TODO Exercise (a)
public class ShutdownMessage extends Message {

	private static final long serialVersionUID = 1L;

	public ShutdownMessage() {
		super(MessageType.SHUTDOWN);
	}

}

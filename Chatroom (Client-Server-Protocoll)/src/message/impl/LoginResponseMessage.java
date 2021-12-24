package message.impl;

import message.Message;
import message.MessageType;

//TODO Exercise (a)
public class LoginResponseMessage extends Message {

	private static final long serialVersionUID = 1L;

	private boolean successful;

	public LoginResponseMessage(boolean successful) {
		super(MessageType.LOGIN_RESPONSE);

		this.successful = successful;
	}

	public boolean isSuccessful() {
		return successful;
	}

}

package message.impl;

import message.Message;
import message.MessageType;

//TODO Exercise (a)
public class LoginRequestMessage extends Message {

	private static final long serialVersionUID = 1L;

	private String username;

	public LoginRequestMessage(String username) {
		super(MessageType.LOGIN_REQUEST);

		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

}

package message.impl;

import message.Message;
import message.MessageType;

//TODO Exercise (a)
public class ChatMessage extends Message {

	private static final long serialVersionUID = 1L;

	private String author;
	private String content;

	public ChatMessage(String author, String content) {
		super(MessageType.CHAT);

		this.author = author;
		this.content = content;
	}

	public String getAuthor() {
		return this.author;
	}

	public String getContent() {
		return this.content;
	}

}

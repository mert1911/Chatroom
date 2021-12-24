package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import message.Message;
import message.impl.ChatMessage;
import message.impl.LoginRequestMessage;
import message.impl.LoginResponseMessage;

public class ChatServerProtocol extends Thread {

	private Socket socket;
	private ObjectOutputStream zumClient;
	private ObjectInputStream vomClient;
	private boolean active;
	private String username;

	public ChatServerProtocol(Socket socket) {
		this.socket = socket;
		this.active = true;

		try {
			this.zumClient = new ObjectOutputStream(socket.getOutputStream());
			this.vomClient = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ioException) {
			System.out.println("IO-Error");
			ioException.printStackTrace();
		}
	}

	private void disconnect() {
		try {
			this.vomClient.close();
			this.zumClient.close();
			this.socket.close();
		} catch (IOException ioException) {
			System.out.println("IO-Error");
			ioException.printStackTrace();
		}
		System.out.println("Protokoll beendet");
	}

	// TODO Exercise (b)
	public void run() {
		while (this.active) {
			try {
				Message message = (Message) this.vomClient.readObject();

				switch (message.getType()) {
				case LOGIN_REQUEST:
					this.username = ((LoginRequestMessage) message).getUsername();

					if (ChatServer.containsUser(this.username)) {
						LoginResponseMessage loginResponseMessage = new LoginResponseMessage(false);
						this.zumClient.writeObject(loginResponseMessage);
					} else {
						ChatServer.addUser(this.username, this.zumClient);
						LoginResponseMessage loginResponseMessage = new LoginResponseMessage(true);
						this.zumClient.writeObject(loginResponseMessage);
					}

					break;
				case CHAT:
					String author = ((ChatMessage) message).getAuthor();
					String content = ((ChatMessage) message).getContent();

					ChatServer.broadcast(author, content);
					break;
				case LOGOUT:
					ChatServer.removeUser(this.username);
					this.active = false;
					break;
				default:
					break;
				}
			} catch (IOException | ClassNotFoundException exception) {
				exception.printStackTrace();
			}
		}

		this.disconnect();
	}

}

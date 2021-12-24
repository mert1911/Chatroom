package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import general.Parameters;
import message.Message;
import message.impl.ChatMessage;
import message.impl.LoginRequestMessage;
import message.impl.LoginResponseMessage;
import message.impl.LogoutMessage;

public class ChatClient implements Runnable {

	private Socket socket;
	private ObjectOutputStream zumServer;
	private ObjectInputStream vomServer;
	private ClientUI ui;
	private String username;
	private Thread thread;
	private boolean active;

	public ChatClient() {
		this.active = true;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ChatClient.this.ui = new ClientUI(ChatClient.this);
				ChatClient.this.ui.setVisible(true);
			}
		});

	}

	private boolean connect() {
		if (this.socket == null || this.socket.isClosed()) {
			try {
				this.socket = new Socket(Parameters.host, Parameters.port);
				this.zumServer = new ObjectOutputStream(socket.getOutputStream());
				this.vomServer = new ObjectInputStream(socket.getInputStream());
				this.active = true;
				this.startThread();
			} catch (UnknownHostException unknownHostException) {
				System.out.println("Kein DNS-Eintrag fuer " + Parameters.host);
				return false;
			} catch (IOException ioException) {
				System.out.println("IO-Error when trying to connect to server");
				System.out.println("No server listening at " + Parameters.host + ":" + Parameters.port);
				return false;
			}
		}

		return true;
	}

	private void startThread() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	void stopClient() {
		if (this.thread != null) {
			this.active = false;
			System.out.println("Interrupt!");
			this.thread.interrupt();
			this.thread = null;
		}
	}

	public void exit() {
		this.stopClient();
		this.disconnect();
		System.exit(0);
	}

	// TODO Exercise (c)
	void register(String username) {
		if (this.connect()) {
			this.username = username;
			Message loginRequestMessage = new LoginRequestMessage(this.username);

			try {
				this.zumServer.writeObject(loginRequestMessage);
			} catch (IOException ioException) {
				System.out.println("Error when sending message type " + loginRequestMessage.getType());
				System.out.println(ioException.getClass().getName());
				ioException.printStackTrace();
			}
		}
	}

	// TODO Exercise (c)
	void disconnect() {
		Message logoutMessage = new LogoutMessage();

		try {
			this.zumServer.writeObject(logoutMessage);
			this.zumServer.close();
			this.vomServer.close();
			this.socket.close();
		} catch (IOException ioException) {
			System.out.println("Error when sending message type " + logoutMessage.getType());
			System.out.println(ioException.getClass().getName());
			ioException.printStackTrace();
		}
	}

	// TODO Exercise (c)
	void sendMessage(String content) {
		Message chatMessage = new ChatMessage(this.username, content);

		try {
			this.zumServer.writeObject(chatMessage);
		} catch (IOException ioException) {
			System.out.println("Error when sending message type " + chatMessage.getType());
			System.out.println(ioException.getClass().getName());
			ioException.printStackTrace();
		}
	}

	// TODO Exercise (c)
	public void run() {
		while (this.active && !(this.socket.isClosed()) && !(this.socket.isInputShutdown())) {
			try {
				Message message = (Message) this.vomServer.readObject();

				switch (message.getType()) {
				case LOGIN_RESPONSE:
					if (((LoginResponseMessage) message).isSuccessful()) {
						this.ui.showChatPanels();
					} else {
						this.ui.showInfoMessage("username: " + this.username + " is already used!");
					}
					break;
				case CHAT:
					String author = ((ChatMessage) message).getAuthor();
					String content = ((ChatMessage) message).getContent();
					this.ui.addMessage(author, content);
					break;
				case SHUTDOWN:
					this.active = false;
					break;
				default:
					break;
				}
			} catch (IOException | ClassNotFoundException exception) {
				exception.printStackTrace();
			}
		}

		this.thread = null;
	}

}

package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;

import general.Parameters;
import message.Message;
import message.impl.ChatMessage;

public class ChatServer extends Thread {

	private static HashMap<String, ObjectOutputStream> outputStreams = new HashMap<String, ObjectOutputStream>();
	private static HashSet<String> users = new HashSet<String>();

	private ServerSocket serverSocket;

	public ChatServer() {
		this.start();
	}

	public void run() {
		try (ServerSocket server = new ServerSocket(Parameters.port)) {
			this.serverSocket = server;
			System.out.println("Server laeuft");
			Socket socket;

			while (true) {
				socket = server.accept();
				new ChatServerProtocol(socket).start();
			}
		} catch (SocketException socketException) {
			System.out.println("Socket closed");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void stopServer() {
		try {
			if (!this.serverSocket.isClosed()) {
				System.out.println("stopping server......");
				broadcast("server", "Server going down immediately");
				ChatServer.users.clear();
				ChatServer.outputStreams.clear();
				this.serverSocket.close();
			}
		} catch (SocketException socketException) {
			System.out.println("Server stopped");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// TODO Exercise (b)
	public static synchronized void addUser(String username, ObjectOutputStream outputStream) {
		ChatServer.users.add(username);
		ChatServer.outputStreams.put(username, outputStream);
	}

	// TODO Exercise (b)
	public static synchronized boolean containsUser(String username) {
		return ChatServer.users.contains(username);
	}

	// TODO Exercise (b)
	public static synchronized void removeUser(String username) {
		ChatServer.users.remove(username);
		ChatServer.outputStreams.remove(username);
	}

	// TODO Exercise (b)
	public static synchronized void broadcast(String author, String content) {
		Message message = new ChatMessage(author, content);

		for (String username : ChatServer.users) {
			ObjectOutputStream outputStream = ChatServer.outputStreams.get(username);

			try {
				outputStream.writeObject(message);
			} catch (IOException ioException) {
				System.out.println("Could not send message to client " + username);
				ioException.printStackTrace();
			}
		}
	}

}

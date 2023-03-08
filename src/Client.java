import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

class Client implements Runnable {
	private final Socket socket;
	Scanner in;
	private PrintStream out;
	private final ChatServer chatServer;
	private String username = "";
	private ArrayList<Client> clients;

	public Client(Socket socket, ChatServer chatServer){
		this.socket = socket;
		new Thread(this).start();
		this.chatServer = chatServer;
	}

	public String getUsername() {
		return this.username;
	}

	void recieve(String message) {
		out.println(message);
	}

	public void run() {
		String input = "";
		clients = chatServer.getClients();
		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			in = new Scanner(is);
			out = new PrintStream(os);

			out.println("Welcome to chat!\n" + "Supported commands: '/bye' - quit.\n" +
					"These users here:");
			for (Client client : clients) {
				out.println(client.getUsername());
			}

			boolean unique = false;
			while (!unique) {
				out.print("Enter unique username:");
				input = in.nextLine();
				if (!clients.isEmpty()) {
					for (Client client : clients) {
						if (client.getUsername().equals(input)) {
							break;
						}
					}
				}
				unique = true;
			}

			this.username = input;

			while (!input.equals("/bye")) {
				input = in.nextLine();
				chatServer.sendMessageToAll(this.username + ">>>  " + input, this);
			}
			chatServer.clientExit(this);
			chatServer.sendMessageToAll("User " + this.username + " left from chat.", this);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
	ArrayList<Client> clients = new ArrayList<>();
	ServerSocket serverSocket;

	void sendMessageToAll(String message, Client besides) {
		for (Client client : clients) {
			if (!(client == besides)) {
				client.recieve(message);
			}
		}
	}

	void clientExit(Client client) {
		clients.remove(client);
	}

	ArrayList<Client> getClients() {
		return clients;
	}

	public ChatServer() throws IOException {
		serverSocket = new ServerSocket(1234);
	}

	public void run() throws IOException {
		while(true) {
			System.out.println("Waiting new client connection");
			try {
				Socket socket = serverSocket.accept();
				clients.add(new Client(socket, this));
				System.out.println("Client connected!");
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new ChatServer().run();
	}
}

package fpcc.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import fpcc.protocol.Message;

public class Server implements Runnable {
	ServerSocket server_socket;

	public Server(int port) {
		try {
			server_socket = new ServerSocket(port);
		} catch (IOException e) {
			throw new RuntimeException("Porta ja utilizada!");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket clientSocket = server_socket.accept();
				System.out
						.println("T" + Thread.activeCount() + " - Cliente conectado: " + clientSocket.getInetAddress());

				while (Thread.activeCount() == Runtime.getRuntime().availableProcessors()) {
					synchronized (this) {
						wait();
						System.out.println("acordei");
					}

				}
				System.out.println("Criando thread");
				new Thread(new SocketProcessing(clientSocket, this)).start();
			} catch (IOException | InterruptedException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}

	}

	public class SocketProcessing implements Runnable {
		private Socket clientSocket;
		private Server parent;

		public SocketProcessing(Socket clientSocket, Server parent) {
			this.clientSocket = clientSocket;
			this.parent = parent;
		}

		@Override
		public void run() {
			try {
				// Obtem os canais de stream
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream();
				PrintWriter pw = new PrintWriter(out);
				BufferedReader inbuffer = new BufferedReader(new InputStreamReader(in));

				// Seção de leitura do request
				String resourcePath = "";

				String data = inbuffer.readLine();
				if (data == null) {
					data = "";
				}
				String[] t = data.split(" ");
				if (t.length == 3)
					resourcePath = t[1].substring(1);

				Message responseMessage = new Message();
				// Seção da leitura do recurso solicitado
				try {
					if (resourcePath.isEmpty())
						resourcePath += "index.html";
					System.out.println("Arquivo aberto");
					File f = new File(resourcePath);
					FileInputStream reader = new FileInputStream(f);
					byte[] array = new byte[(int) f.length()];
					reader.read(array);
					responseMessage.setContent(new String(array, "UTF-8"));
					reader.close();
					// configura estado pra ok
					responseMessage.setStatus(Message.OK);

				} catch (FileNotFoundException e) {
					responseMessage.setStatus(Message.NOT_FOUND);
				}

				pw.print(responseMessage);
				pw.flush();

				pw.close();
				inbuffer.close();
				clientSocket.close();
				System.out.println("Conexao encerrada");
				synchronized (parent) {
					parent.notify();
				}
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}

		}
	}

}
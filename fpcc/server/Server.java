package fpcc.server;

import java.io.BufferedReader;
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

	// TODO: TIRAR ESSE CODIGO DO RUN, um main ja serve
	@Override
	public void run() {
		while (true) {
			try {
				Socket clientSocket = server_socket.accept();
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
				// Obtem os canais de stream
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream();
				PrintWriter pw = new PrintWriter(out);
				BufferedReader inbuffer = new BufferedReader(new InputStreamReader(in));

				boolean headerFinished = false;
				Message responseMessage = new Message();
				String data = "";
				String resourcePath = "";

				byte i = 0;
				do {
					data = inbuffer.readLine();
					if (data == null) {
						data = "";
					}

					if (i == 0) {
						// Primeiro campo, onde contem o metodo, recurso desejado e protocolo
						// Quebrar:
						String[] t = data.split(" ");
						if (t.length == 3)
							resourcePath = t[1];
					}

					//
					System.out.println(i);
					System.out.println(data);
					//
					if (data.isEmpty()) {
						headerFinished = true;
					}
					// Se tiver lendo o content do request salva no content do response
					if (headerFinished) {
						responseMessage.setContent(data);
					}
					i++;
				} while (inbuffer.ready());

				// configura estado pra ok
				responseMessage.setStatus(Message.OK);
				// sendo content vazio entao responde com mensagem padrao
				if (responseMessage.isEmpty()) {
					responseMessage.setContent("200 OK - " + resourcePath);
				}
				// Vamos ver como esta o pacote...
				// System.out.println(responseMessage.toString());
				// envia o pacote montado
				pw.print(responseMessage.toString());
				pw.flush();

				System.out.println("Conexao encerrada");
				inbuffer.close();
				clientSocket.close();
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
}
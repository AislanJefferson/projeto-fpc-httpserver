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
	private static final String WEBROOT = "public_html";

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

				// Seção de leitura do request
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
					if (data.isEmpty()) {
						headerFinished = true;
					}

					System.out.println(i + " - " + data);
					// Se tiver lendo o content do request salva no content do response
					// if (headerFinished) {
					// responseMessage.setContent(data);
					// }
					i++;
				} while (!headerFinished && inbuffer.ready());

				// Seção da leitura do recurso solicitado
				try {
					if (resourcePath.equals("/"))
						resourcePath += "index.html";
					// ARQUIVOS DEVEM FICAR NA PASTA public_html
					File f = new File(Server.WEBROOT + resourcePath);
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
				// Vamos ver como esta o pacote...
				// System.out.println(responseMessage.toString());
				// envia o pacote montado
				pw.print(responseMessage);
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
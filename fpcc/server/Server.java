package fpcc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
				System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
				// Obtem os canais de stream
				InputStream in = clientSocket.getInputStream();
				OutputStream out = clientSocket.getOutputStream();

				// Realiza o recebimento da mensagem e decodificacao
				byte[] buffer = new byte[Message.DATA_LENGTH];
				// Apenas decodifica se tiver lido a qtde de bytes de um pacote
				// de mensagem no qual é 5 bytes
				Message msg = new Message();
				while ((in.read(buffer, 0, Message.DATA_LENGTH)) == Message.DATA_LENGTH) {
					msg.setFromBytes(buffer);
					if (!msg.isEmpty()) {
						int valueToSend = 0;
						int opToSend;
						switch (msg.getOpCode()) {
						case Message.DEC:
							valueToSend = msg.getValue() - 1;
							opToSend = Message.OK;
							break;
						case Message.INC:
							valueToSend = msg.getValue() + 1;
							opToSend = Message.OK;
							break;
						default:
							opToSend = Message.ERR;
							break;
						}
						// TODO: FUNCAO DE LEITURA E ENVIO DE MENSAGEM COMO
						// sendMessage(Message msg) da classe Client
						out.write((new Message(opToSend, valueToSend)).getBytes());
					}

				}

				System.out.println("Conexao encerrada");
				clientSocket.close();
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
}
package fpcc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import fpcc.protocol.Message;

public class Client implements Runnable {
	private String host;
	private int port;
	Socket conexao;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		conexao = new Socket();
	}

	// Apenas envia mensagens nao vazias
	protected boolean sendMessage(Message msg) {
		boolean success = false;
		if (!msg.isEmpty() && conexao.isConnected()) {
			OutputStream out;
			try {
				out = conexao.getOutputStream();
				out.write(msg.getBytes());
				success = true;

			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
		return success;

	}

	protected Message readMessage() {
		Message msg = new Message();
		if (conexao.isConnected()) {
			InputStream in;
			try {
				in = conexao.getInputStream();
				byte[] buffer = new byte[Message.DATA_LENGTH];
				in.read(buffer, 0, Message.DATA_LENGTH);
				msg.setFromBytes(buffer);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		return msg;
	}

	@Override
	public void run() {
		try {
			conexao = new Socket(host, port);
			System.out.println("Connectado? " + conexao.isConnected());
			System.out.println("-----------");
			// Leitura da entrada padrao
			String entrada;
			Scanner sc = new Scanner(System.in);
			System.out.println("Comando:");

			Message msgToSend = new Message();
			while ((!(entrada = sc.nextLine()).toLowerCase().equals("exit"))) {
				// Decodificacao da entrada
				String[] entradaQuebrada = entrada.split(" ");
				if (entradaQuebrada.length == 2) {
					int op;
					switch (entradaQuebrada[0].toUpperCase()) {
					case "DEC":
						op = Message.DEC;
						break;
					case "INC":
						op = Message.INC;
						break;
					default:
						op = Message.NIL;
						break;
					}

					// Monta a mensagem para envio ao servidor
					try {
						msgToSend.setValue(Integer.parseInt(entradaQuebrada[1]));
					} catch (NumberFormatException nfe) {
						op = Message.NIL;
					}
					msgToSend.setOpCode(op);

					if (sendMessage(msgToSend)) {
						// Etapa de leitura do servidor
						Message msg = readMessage();
						if (!msg.isEmpty() && msg.getOpCode() == Message.OK) {
							// Apenas exibe se a mensagem recebida foi
							// processada
							// pelo servidor

							System.out.println("Resposta: " + msg.getValue());

						}
					}
					System.out.println("Comando:");
				}
			}
			System.out.println("Desconectado!");
			sc.close();
			conexao.close();

		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

}

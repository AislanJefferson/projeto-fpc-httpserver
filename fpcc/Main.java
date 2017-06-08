package fpcc;

import fpcc.client.Client;
import fpcc.server.Server;

public class Main {

	public static void main(String[] args) {
		// Execucao na forma: java -jar programa.jar -s <porta> para modo servidor
		// ou: java -jar programa.jar -c <ip> <porta> para modo cliente
		// TODO: Modificar pre-processamento/decodificacao dos argumentos
		if (args.length == 2 || args.length == 3) {
			int port;
			switch (args[0].toLowerCase().split("-")[1]) {
			case "c":
				String ip = args[1];
				port = Integer.parseInt(args[2]);
				System.out.println("Client mode");
				System.out.println("-----------");
				Client c = new Client(ip, port);
				c.run();

				break;
			case "s":
				System.out.println("Server mode");
				port = Integer.parseInt(args[1]);
				Server s = new Server(port);
				s.run();
			default:
				break;
			}
		} else {
			System.out.println("Dica de utilizacao:");
			System.out.println("\tPara modo servidor executar na forma \"programa.jar -s <porta>\"");
			System.out.println("\tPara modo cliente executar na forma \"programa.jar -c <ip> <porta>\"");
		}
	}
}
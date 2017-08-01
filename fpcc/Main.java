package fpcc;

import fpcc.server.Server;

public class Main {

	public static void main(String[] args) {
		System.out.println("Server mode");
		int port = 8080;
		if (args.length == 1)
			port = Integer.parseInt(args[0]);
		Server s = new Server(port);
		System.out.println("Executando na porta " + port);
		s.run();
	}
}
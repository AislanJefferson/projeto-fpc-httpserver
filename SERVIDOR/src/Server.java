
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.net.*;


public class Server {
	
	public static void main(String args[]){
        try {
        	// porta do server  
            ServerSocket server = new ServerSocket(3322);                       
            System.out.println("Servidor iniciado na porta 3322");
            
            // escuta uma conexão e aceita se alguma for encontrada. retorna um objeto  SOCKET
            Socket cliente = server.accept();
            //Retorna o endereço de IP local do servidor
            System.out.println("Cliente conectado do IP "+cliente.getInetAddress().
                    getHostAddress());
            Scanner entrada = new Scanner(cliente.getInputStream());
            
            // Enviando dados ao servidor
            while(entrada.hasNextLine()){
                System.out.println(entrada.nextLine());
            }
            
            entrada.close();
            server.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

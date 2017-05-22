package server_rmi;

import java.io.IOException;

import static server_rmi.Compute.PORT;

public class Main{

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer(PORT);

        System.out.println(" Server starting");
        System.out.println(" Please, enter any button to exit from server");


        System.in.read();
        server.closeServer();

        System.out.println(" Server closed");
    }
}

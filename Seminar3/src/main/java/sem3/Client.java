package sem3;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import lpi.server.rmi.IServer;

public class Client {

    public static boolean flug = true;

    public void start(int port) {

        try (Scanner scanner = new Scanner(System.in)) {

            Registry registry = LocateRegistry.getRegistry(port);
            IServer proxy = (IServer) registry.lookup(IServer.RMI_SERVER_NAME);
            System.out.println("Welcome to server");
            Handler inter = new Handler(proxy);
            
            while (flug) {
                String inLine = scanner.nextLine().trim();
                
                if (!inLine.equals("")) {
                    inter.handler(inLine);
                }
            }

        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Problem connection");
        }
    }
	

}

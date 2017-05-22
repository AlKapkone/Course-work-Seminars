package rmi_client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import server_rmi.Compute;

public class Start {

    private final Interpretation interpretation;
    private final Registry registry;
    private final Compute remoteCompute;

    public static boolean flag = true;

    public Start() throws Exception {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        registry = LocateRegistry.getRegistry(Compute.PORT);
        remoteCompute = (Compute) registry.lookup(Compute.SERVER_NAME);
        interpretation = new Interpretation(remoteCompute);
    }

    public void waitCommand() {

        System.out.println("Welcome to Al_Kapkone_server");
        Scanner scanner = new Scanner(System.in);

        while (flag)
            interpretation.interpretation(scanner.nextLine());

        scanner.close();
    }
}

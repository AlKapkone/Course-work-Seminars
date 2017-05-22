package server_rmi;

import java.rmi.registry.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Compute {

    private int port;
    private Registry registry;
    private Compute stub;

    public void startServer(int port){
        this.port = port;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            registry = LocateRegistry.createRegistry(port);
            stub = (Compute) UnicastRemoteObject.exportObject(this, port);
            registry.rebind(Compute.SERVER_NAME, stub);

        } catch (Exception ex) {
            System.err.println("ComputeEngine exception: ");
        }
    }

    @Override
    public String ping() throws RemoteException {
        return "pong";
    }

    @Override
    public String echo(String text) throws RemoteException {
        return "RESPONSE from rmi server: " + text;
    }

    @Override
    public <T> T executeTask(Task<T> t) throws IOException, RemoteException {
        return t.execute();
    }

    public void closeServer() throws IOException{
        if (registry != null) {
            try {
                registry.unbind(SERVER_NAME);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            registry = null;
        }

        if (stub != null) {
            UnicastRemoteObject.unexportObject(this, true);
            stub = null;
        }
    }
}

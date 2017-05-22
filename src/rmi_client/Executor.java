package rmi_client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;

import server_rmi.Compute;
import server_rmi.Compute.HeapSort;
import server_rmi.Compute.FileInfo;

public class Executor {

    private static final int SIZE_ELEMENTS = 500_000;
    private static final int MAX_VALUE = 1_000_000_000;

    private final Compute remoteCompute;

    public Executor(Compute remoteCompute) {
        this.remoteCompute = remoteCompute;
    }

    public void exit() throws RemoteException {
        System.out.println("Exit from server");
        Start.flag = false;
    }

    public void ping() throws RemoteException {
        System.out.println(remoteCompute.ping());
    }

    public void echo(String[] parameters) throws RemoteException {
        if(parameters.length != 2) {
            System.out.println("Bad argument");
            return;
        }
        System.out.println(remoteCompute.echo(parameters[1]));
    }

    public void sort(String[] parameters) throws RemoteException, IOException {
        if(parameters.length != 2){
            System.out.println("Bad argument");
            return;
        }

        String[] stringMass = new String[SIZE_ELEMENTS];

        for (int i = 0; i < stringMass.length; i++) {
            int value = (int) (Math.random() * MAX_VALUE);
            stringMass[i] = String.valueOf(value);
        }
        FileInfo sandedFileInfo = new FileInfo(write(parameters[1], stringMass));

        System.out.println("Send file to server and wait for response");
        FileInfo fileInfo = remoteCompute.executeTask(new HeapSort(sandedFileInfo));
        System.out.println("Sorting was successful");

        System.out.println("Prepare to record file");
        String content = new String(fileInfo.getFileContent(), StandardCharsets.UTF_8);

        System.out.println("Recording in progress......");
        write(fileInfo.getFilename(), content.split(" "));
        System.out.println("File successfully recorded");
    }

    private File write(String fileName, String[] writeMass) {
        File file = new File(fileName);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (String element : writeMass) {
                dos.writeBytes(element + " ");
            }
        } catch (Exception ex) {
            System.out.println("There was a problem with the recording");
        }
        return file;
    }
}

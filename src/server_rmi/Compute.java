package server_rmi;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;

public interface Compute extends Remote {

    public static final int PORT = 15151;
    public static final String SERVER_NAME = "Al_Kapkone_server";

    public <T> T executeTask(Task<T> t) throws IOException, RemoteException;

    public String echo(String text) throws RemoteException;

    public String ping() throws RemoteException;



    public static class FileInfo implements Serializable {

        private static final long serialVersionUID = 229L;
        private byte[] fileContent;
        private String filename;

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public FileInfo() {
        }

        public FileInfo(File file) throws IOException {
            fileContent = Files.readAllBytes(file.toPath());
            filename = file.getName();
        }

        public byte[] getFileContent() {
            return fileContent;
        }

        public void setFileContent(byte[] fileContent) {
            this.fileContent = fileContent;
        }
    }



    public class HeapSort implements Task<Compute.FileInfo>, Serializable {

        private static final long serialVersionUID = 227L;

        private final Compute.FileInfo fileInfo;
        private String[] stringMass;
        private int[] intMass;
        private File file;

        public HeapSort(FileInfo fileInfo) throws IOException {
            this.fileInfo = fileInfo;
        }

        @Override
        public Compute.FileInfo execute() throws IOException {

            String content = new String(fileInfo.getFileContent(), StandardCharsets.UTF_8).trim();
            stringMass = (content.equals("")) ? new String[]{} : content.split(" ");

            if(stringMass.length == 0){
                intMass = new int[]{};
            } else {
                intMass = new int[stringMass.length];
                for (int i = 0; i < stringMass.length; i++) {
                    intMass[i] = Integer.parseInt(stringMass[i]);
                }
            }

            int start = (int) (Instant.now().getEpochSecond());
            System.out.println("Start sort");
            heapSort(intMass);
            int finish = (int) (Instant.now().getEpochSecond() - start);

            System.out.println(" sort continued " + finish + " seconds");
            file = new File("fromServer_" + fileInfo.getFilename());

            if (intMass.length != 0) {
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                    for (int element : intMass) {
                        dos.writeBytes(element + " ");
                    }
                } catch (Exception ex) {
                    System.out.println("Crash write file");
                }
            }
            System.out.println("Sending response");
            return new Compute.FileInfo(file);
        }

        private void heapSort(int[] mas) {
            int array_size = mas.length;
            // Формируем нижний ряд пирамиды
            for (int i = (array_size / 2) - 1; i >= 0; i--) {
                siftDown(mas, i, array_size);
            }
            // Просеиваем через пирамиду остальные элементы
            for (int i = array_size - 1; i >= 1; i--) {
                int temp = mas[0];
                mas[0] = mas[i];
                mas[i] = temp;
                siftDown(mas, 0, i - 1);
            }
        }

        private void siftDown(int[] mas, int element1, int element2) {
            int maxChild; // индекс максимального потомка
            boolean done = false; // флаг того, что куча сформирована
            // Пока не дошли до последнего ряда
            while ((element1 * 2 <= element2) && (!done)) {

                if((element1 * 2 + 1) >= mas.length)
                    break;

                if (element1 * 2 == element2) // если мы в последнем ряду,
                {
                    maxChild = element1 * 2;    // запоминаем левый потомок
                } // иначе запоминаем больший потомок из двух
                else if (mas[element1 * 2] > mas[element1 * 2 + 1]) {
                    maxChild = element1 * 2;
                } else{
                    maxChild = element1 * 2 + 1;
                }
                // если элемент вершины меньше максимального потомка
                if (mas[element1] < mas[maxChild]) {
                    int temp = mas[element1]; // меняем их местами
                    mas[element1] = mas[maxChild];
                    mas[maxChild] = temp;
                    element1 = maxChild;
                } else // иначе
                    done = true; // пирамида сформирована
            }
        }
    }
}

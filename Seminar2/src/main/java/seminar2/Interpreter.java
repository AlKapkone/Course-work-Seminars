package seminar2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Interpreter {

    public static String sendCommand = null;

    CommandProcessingToServer comTo = new CommandProcessingToServer();

    public byte[] interpreter(String inLine) throws IOException {

        String[] commandMas = parseForCommand(inLine);

        switch (commandMas[0]) {
            case "ping":
                return comTo.pingToServer(commandMas[0]);

            case "echo":
                return comTo.ehoToServer(commandMas);

            case "login":
                return comTo.loginToServer(commandMas);

            case "list":
                return comTo.listToServer(commandMas);

            case "msg":
                return comTo.msgToServer(commandMas);

            case "file":
                return comTo.fileToServer(commandMas);

            case "receivemsg":
                return comTo.receiveMsgToServer();

            case "receivefile":
                return comTo.receiveFileToServer();

            default:
                System.out.println("Such a command does not exist");
                return null;
        }
    }
    
    CommandProcessingFromServer comFrom = new CommandProcessingFromServer();

    public void interpreter(byte[] serverResp) {
        if (serverResp != null) {
            try {
                switch (sendCommand) {
                    case "ping":
                        comFrom.pingFromServer(serverResp);
                        break;

                    case "echo":
                        comFrom.ehoFromServer(serverResp);
                        break;

                    case "login":
                        comFrom.loginFromServer(serverResp);
                        break;

                    case "list":
                        comFrom.listFromServer(serverResp);
                        break;

                    case "msg":
                        comFrom.msgFromServer(serverResp);
                        break;

                    case "file":
                        comFrom.fileFromServer(serverResp);
                        break;

                    case "receivemsg":
                        comFrom.receiveMsg(serverResp);
                        break;

                    case "receivefile":
                        comFrom.receiveFile(serverResp);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("problem with interpretation responds");
            }
        }
    }

    public static byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(object);
            return byteStream.toByteArray();
        }
    }

    public static <T> T deserialize(byte[] data, int offset, Class<T> clazz) throws ClassNotFoundException, IOException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data, offset, data.length - offset);
                ObjectInputStream objectStream = new ObjectInputStream(stream)) {
            return (T) objectStream.readObject();
        }
    }

    private String[] parseForCommand(String line) {

        String[] outMas = null;
        String[] parsMas = line.split(" ", 2);

        switch (parsMas[0]) {
            case "echo":
                outMas = line.split(" ", 2); // command _ anyText
                break;

            case "msg":
                outMas = line.split(" ", 3); // command _ destination _ messageText
                break;

            default:
                outMas = line.split(" ");
                break;
        }
        return outMas;
    }
}

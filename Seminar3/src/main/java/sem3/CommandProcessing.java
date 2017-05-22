package sem3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lpi.server.rmi.IServer;

public class CommandProcessing {


    private final IServer iServ;

    public CommandProcessing(IServer iServ) {
        this.iServ = iServ;
    }

    public void ping(){        
        try {
            iServ.ping();
            System.out.println("Ping successful");
        } catch (Exception ex) {
            System.out.println("connections problem");
        }
    }

    public void echo(String[] comandMas){
    	try {
    		if (comandMas.length == 2) {
                System.out.println(iServ.echo(comandMas[1]));
            } else {
                System.out.println("Please enter the text to be transferred");
            }
        } catch (Exception ex) {
            System.out.println("Problems with connection");
        }
    }

    private final Timer timer = new Timer();
    private static String myLogin = null;
    private static String sessionId = null;

    public void login(String[] commandMas) throws RemoteException {
        if (commandMas.length == 3) {

            if (!commandMas[1].equals(myLogin)) {

                if (sessionId != null) {
                    iServ.exit(sessionId);
                }

                sessionId = iServ.login(commandMas[1], commandMas[2]);

                if (myLogin == null) {
                    timer.schedule(receive, 0, 1500);
                }
                myLogin = commandMas[1];
            }
        } else {
            System.out.println("Bad argument");
        }
    }

    public void list() throws RemoteException {

        if (sessionId != null) {
            String[] list = iServ.listUsers(sessionId);
            if (list != null) {
                for (String f : list) {
                    System.out.println(f);
                }
            }
        } else {
            System.out.println("No login");
        }
    }

    public void msg(String[] comandMas) throws RemoteException {
        if (sessionId == null) {
            System.out.println("No login");
        } else if (comandMas.length == 3) {

            if (isItUser(comandMas[1])) {
                iServ.sendMessage(sessionId, new IServer.Message(comandMas[1], myLogin, comandMas[2]));
                System.out.println("Message sent");
            }

        } else {
            System.out.println("bad argument");
        }

    }

    public void file(String[] commandMas) throws IOException {

        if (sessionId == null) {
            System.out.println("No login");
        } else if (commandMas.length == 3) {
            File fil = new File(commandMas[2]);

            if (isItUser(commandMas[1])) {
                if (fil.exists()) {
                    iServ.sendFile(sessionId, new IServer.FileInfo(commandMas[1], fil));
                    System.out.println("File sent");
                } else {
                    System.out.println("No this file");
                }
            }
        } else {
            System.out.println("bad argument");
        }
    }

    public void receiveMsg() throws RemoteException {
        if (sessionId != null) {
            IServer.Message mess = null;
            mess = iServ.receiveMessage(sessionId);
            if (mess != null) {
                System.out.println("Message from: " + mess.getSender() + " : " + mess.getMessage());
            } else if (flug) {
                System.out.println("No message");
            }
        } else {
            System.out.println("No login");
        }
    }

    public void receiveFile() throws RemoteException {
        IServer.FileInfo file = null;
        
        if (sessionId != null) {
            file = iServ.receiveFile(sessionId);
            
            if (file != null) {
                System.out.println(file.toString());

                try (FileOutputStream fos = new FileOutputStream(
                        new File(file.getSender() + "_" + file.getFilename()))
                        ) {
                    fos.write(file.getFileContent());
                } catch (Exception ex) {
                    System.out.println("Problem with write file");
                }
            } else if (flug) {
                System.out.println("No file");
            }
        } else {
            System.out.println("No login");
        }
    }

    public void exit() throws RemoteException {
        timer.cancel();
        iServ.exit(sessionId);
        System.out.println("Exit from server");
        Client.flug = false;
    }

    private boolean isItUser(String user) {
        boolean isIt;
        if (this.user.contains(user)) {
            isIt = true;
        } else {
            System.out.println("No this user");
            isIt = false;
        }
        return isIt;
    }

    private boolean flug;

    TimerTask receive = new TimerTask() {

        @Override
        public void run() {
            try {

                flug = false;
                receiveMsg();
                receiveFile();
                activeUser();
                flug = true;

            } catch (RemoteException ex) {
                System.out.println("Problem Timer task");
            }
        }
    };

    private final List<String> user = new LinkedList<>();

    private void activeUser() throws RemoteException {
        if (sessionId != null) {
            List<String> activeUser = new LinkedList<>();
            List<String> loggedOut = new LinkedList<>();

            String[] list = iServ.listUsers(sessionId);

            if (list != null) {

                for (String f : list) {
                    activeUser.add(f);
                    if (!user.contains(f)) {
                        user.add(f);
                        System.out.println(f + " logged in");
                    }
                }

                for (String us : user) {
                    if (!activeUser.contains(us)) {
                        System.out.println(us + " logged out");
                        loggedOut.add(us);
                    }
                }
                for (String out : loggedOut) {
                    user.remove(out);
                }
            }
        }
    }

}

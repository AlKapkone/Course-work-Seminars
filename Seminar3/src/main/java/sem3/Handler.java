package sem3;

import lpi.server.rmi.IServer;

public class Handler {

    private final CommandProcessing comP;
    private final Parser parser = new Parser(); 

    public Handler(IServer ob) {
        comP = new CommandProcessing(ob);        
    }
    
    public void handler(String inLine){

        String[] commandMas = parser.parsForCommand(inLine);

        try {
            switch (commandMas[0]) {

                case "ping":
                    comP.ping();
                    break;

                case "echo":
                    comP.echo(commandMas);
                    break;

                case "login":
                    comP.login(commandMas);
                    break;

                case "list":
                    comP.list();
                    break;

                case "msg":
                    comP.msg(commandMas);
                    break;

                case "file":
                    comP.file(commandMas);
                    break;

                case "receivemsg":
                    comP.receiveMsg();
                    break;

                case "receivefile":
                    comP.receiveFile();
                    break;

                case "exit":
                    comP.exit();
                    break;

                default:
                    System.out.println("No such command");
                    break;
            }
        } catch (Exception ex) {
            System.out.println("Handler problem");
            ex.printStackTrace();
        }
    }
}

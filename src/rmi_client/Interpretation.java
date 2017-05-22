package rmi_client;

import server_rmi.Compute;

public class Interpretation {
    private final Executor executor;
    
    public Interpretation(Compute remoteCompute) {
        executor = new Executor(remoteCompute);
    }

    private final Parser parser = new Parser();

    public void interpretation(String inLine) {

        try {
            String[] commandMas = parser.pars(inLine);

            switch (commandMas[0]) {

                case "exit":
                    executor.exit();
                    break;

                case "ping":
                    executor.ping();
                    break;

                case "echo":
                    executor.echo(commandMas);
                    break;

                case "sort":
                    executor.sort(commandMas);
                    break;

                default:
                    System.out.println("Entered wrong command");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

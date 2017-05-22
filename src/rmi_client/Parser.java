package rmi_client;

public class Parser {

    public String[] pars(String parsLine) {

        String[] parsMas = parsLine.split(" ", 2);

        if(parsMas[0].equals("echo"))
            return parsMas;
        else
            return parsLine.split(" ");
    }
}

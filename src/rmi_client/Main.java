package rmi_client;

public class Main {
    
    public static void main(String[] args) {        
        try {            
            Start start = new Start();
            start.waitCommand();
        } catch (Exception ex) {
            ex.printStackTrace();
        }      
    }
}

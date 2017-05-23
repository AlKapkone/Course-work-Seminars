package seminar6;

public class Starter {

    public static void main(String[] args) {
        
        try(Client client = new Client()){
            client.start();
        }        
        
    }    
}

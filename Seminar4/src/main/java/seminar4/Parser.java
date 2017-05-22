package seminar4;

public class Parser {
    
    public String[] parsForComand(String line) {
        String[] outMas;
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

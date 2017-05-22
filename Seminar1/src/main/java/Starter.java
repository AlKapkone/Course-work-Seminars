import java.util.Scanner;

public class Starter {
	
	public static boolean myVar = true;

	public static void main(String[] args) {
		
		System.out.println("Hello World!!!");
		
		Scanner scanner = new Scanner(System.in);

		Interpreter intObj = new Interpreter();
		
		while(myVar){

		    System.out.println("\nPlease, Enter command or write \"finish\" to exit :\n");

			if(scanner.hasNextLine()){

			    intObj.process(scanner.nextLine());
			}			
		}

		scanner.close();
	}
}

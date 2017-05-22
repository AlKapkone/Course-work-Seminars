public class Interpreter {
	
	public void process (String line){
		
		if(line.equals("finish")){

			Starter.myVar = false;
		}
		else if (!line.equals("")){
			
			String [] commandMas = line.split(" ");
		
			for (int i = 0; i < commandMas.length; i++){

				if (i == 0){

					System.out.print("Entered command <" + commandMas[i] + ">");

				} else {

					if(i == 1){
						System.out.print(" and parameters");
					}

					System.out.print(" <" + commandMas[i] + ">");
				}
			}		
		}
	}
}

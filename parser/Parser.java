import java.util.*;
import java.io.*;

public class Parser {

	public static void main(String[] args){

		// Take in from STDin
		Vector<String> input_vector = new Vector<String>();
		Scanner file_input = new Scanner(System.in);
		while(file_input.hasNextLine()){
			input_vector.add(file_input.nextLine());
		}
	}

}
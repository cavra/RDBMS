import java.util.*;
import java.io.*;

public class Parser {

	public static void main(String[] args){

		// Take in from STDin
		Vector<String> input_vector = new Vector<String>();
		Scanner file_input = new Scanner(System.in);
		while(file_input.hasNextLine()) {
			String line = file_input.nextLine();
			if (line != null && !line.isEmpty()) {
				input_vector.add(line);
			}
		}

		for (String line : input_vector) {
			Grammar grammy = new Grammar(line);
		}
	}
}
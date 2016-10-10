import java.util.*;
import java.io.*;

public class Parser {

// =============================================================================
// The Main Method call for our RDBMS system. 
//   Essentially, this takes a input from Standard Input, removes empty or null
//   lines, and then passes this data to the Grammar to be parsed. 
// Parameters:
//   args: Needed Parameter for main method call, not used
// =============================================================================

	public static void main(String[] args) {

		// Take in from STDin
		ArrayList<String> input_vector = new ArrayList<String>();
		Scanner file_input = new Scanner(System.in);

		// While the scanner reads in a new line...
		while (file_input.hasNextLine()) {
			String line = file_input.nextLine();

			// If it's not empty or null, record it
			if (line != null && !line.isEmpty()) {
				input_vector.add(line);
			}
		}

		// Pass each line to the Grammar to be parsed
		for (String line : input_vector) {
			Grammar grammy = new Grammar(line);
		}
	}
}
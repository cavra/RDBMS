import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class Grammar {
   
	private Boolean isFound = false;
	private Vector<String> token_vector = new Vector<String>();

	Grammar(String line){

		// Tokenize the input and store in a vector
		String delimiters = "(){}; \t\n\r\f";
		StringTokenizer st = new StringTokenizer(line, delimiters, true);
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			if (!token.trim().isEmpty() && !token.equals(",")) {
				token = token.replaceAll(",", "");
				token_vector.add(token);
			 }
		}

		System.out.println(token_vector);

		for (String token : token_vector){
			switch(token) {
				case "CREATE":
					System.out.println("\nCREATE TABLE invoked");
					createTableCommand(token_vector);
				case "DROP":
				case "OPEN":
				case "ClOSE":
				case "WRITE":
				case "EXIT":
				case "SHOW":
				case "UPDATE":
				case "DELETE":
				case "INSERT":
				case "SELECT":
				case "PROJECT":
				case "RENAME":
			}
		}

		isFound = false;
	}

	// public String buildString(String line, String command){

	// 	String[] ar = {"Hi"};
	// 	String cut_string = line.replace(command, "");

	// 	if (command.contains("CREATE TABLE")) {
	// 		System.out.println("\nCREATE TABLE invoked");
	// 		createTableCommand(cut_string);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("DROP TABLE")) {
	// 		//System.out.println("\nDROPTABLE Found command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("OPEN")) {
	// 		//System.out.println("\nOPEN Found command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("CLOSE")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("WRITE")) {
	// 		System.out.println("\nWrite invoked");
	// 		write_command(cut_string);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("EXIT")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("SHOW")) {
	// 		System.out.println("\nShow invoked");
	// 		show_command(cut_string);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("UPDATE")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("DELETE FROM")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("INSERT INTO")) {
	// 		System.out.println("\nINSERT INTO invoked");
	// 		insert_command(cut_string);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("SELECT")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("PROJECT")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("RENAME")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("+")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("-")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("*")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("JOIN")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("<-")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else if (command.contains("==") || 
	// 		command.contains("!=") || 
	// 		command.contains("<") || 
	// 		command.contains(">") || 
	// 		command.contains("<=") ||
	// 		command.contains(">=")) {
	// 		//System.out.println("\nFound command: " + command);
	// 		isFound = true;
	// 	}
	// 	else {
	// 		//System.out.println("\nNOT found command: " + command);
	// 	}
	// 	//Table new_table = Engine.createTable("hi", ar, ar);
	// 	return command;
	// }

	public static Table createTableCommand(Vector<String> token_vector){	

		String relation_name = "";
		Vector<String> attribtues_vector = new Vector<String>();
		Vector<String> keys_vector = new Vector<String>();

		// Start the token index at the begining of the relation name
		Integer token_index = 2;

		// Get the relation name
		for (int i = token_index; i < token_vector.size(); i++){
			if (token_vector.get(i).equals("(")){
				token_index = i;
				break;
			}
			else {
				relation_name += token_vector.get(i);
			}
		}

		// Get the attribute list
		for (int i = token_index; i < token_vector.size(); i++){
			if (token_vector.get(i).equals("PRIMARY")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("VARCHAR")) {
				i = i + 2;
				continue;
			}
			else if (token_vector.get(i).equals("INTEGER")) {
				continue;
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				attribtues_vector.add(token_vector.get(i));
			}
			else {
				//System.out.println("Skipping token... " + token_vector.get(i));
			}
		}

		// Get the primary keys list
		for (int i = token_index; i < token_vector.size(); i++){
			if (token_vector.get(i).equals(";")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("PRIMARY") || token_vector.get(i).equals("KEY")) {
				continue;
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				keys_vector.add(token_vector.get(i));
			}			
			else {
				//System.out.println("Skipping token... " + token_vector.get(i));				
			}
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute List:" + attribtues_vector);
		System.out.println("Primary keys List:" + keys_vector);

		String[] attributes_array = attribtues_vector.toArray(new String[attribtues_vector.size()]);
		String[] keys_array = keys_vector.toArray(new String[keys_vector.size()]);

		Table new_table = Engine.createTable(relation_name.trim(), attributes_array, keys_array);
		return new_table;
	}

	public static void insert_command(String cut_string){
		String relation_name = "";
		String data_string = "";

		// Get the table name
		String pattern_relation_name = ".+?(?=VALUES FROM)";
		Pattern r1 = Pattern.compile(pattern_relation_name);
		Matcher m1 = r1.matcher(cut_string);
		if (m1.find()) {
			relation_name = m1.group(0).trim();
			cut_string = cut_string.replace(m1.group(0), "");
		}

		// Get the data to insert
		String data_list = "(?<=VALUES FROM).+?(?=;)";
		Pattern r2 = Pattern.compile(data_list);
		Matcher m2 = r2.matcher(cut_string);
		if (m2.find()) {
			data_string = m2.group(0).trim();
			cut_string = cut_string.replace(m2.group(0), "");
		}

		// Parse the data into an array
		String[] data_array = data_string.split(",");
		data_array[0] = data_array[0].replaceFirst("[\\(]", "");
		data_array[data_array.length - 1] = data_array[data_array.length - 1].substring(0,data_array[data_array.length - 1].length()-1);

		System.out.println("Table name: " + relation_name);
		System.out.println("Data String: " + Arrays.toString(data_array));
		System.out.println("");

		// Place data in pre-existing table
		Engine.insertRow(relation_name.trim(), data_array);
	}

	public static void write_command(String cut_string)
	{
		String relation_name = "";
		// Get the table name
		String pattern_relation_name = ".+?(?=;)";
		Pattern r1 = Pattern.compile(pattern_relation_name);
		Matcher m1 = r1.matcher(cut_string);
		if (m1.find()) {
			relation_name = m1.group(0).trim();
			cut_string = cut_string.replace(m1.group(0), "");
		}
		System.out.println("Table Name: " + relation_name);
		Engine.writeTable(relation_name.trim());

	}

	public static void show_command(String cut_string)
	{
		String relation_name = "";
		// Get the table name
		String pattern_relation_name = ".+?(?=;)";
		Pattern r1 = Pattern.compile(pattern_relation_name);
		Matcher m1 = r1.matcher(cut_string);
		if (m1.find()) {
			relation_name = m1.group(0).trim();
			cut_string = cut_string.replace(m1.group(0), "");
		}
		System.out.println("Table Name: " + relation_name);
		Engine.show(relation_name.trim());

	}

}













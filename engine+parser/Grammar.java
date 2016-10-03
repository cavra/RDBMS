import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class Grammar {
   
	Boolean isFound = false;

	Grammar(String line){
		String concat = "";
		for (char c: line.toCharArray()) {
			if (!isFound) {
				concat += c;
				buildString(line, concat);
			}
		}
		isFound = false;
	}

	public String buildString(String line, String command){

		String[] ar = {"Hi"};
		String cut_string = line.replace(command, "");

		if (command.contains("CREATE TABLE")) {
			System.out.println("\nCREATE TABLE invoked");
			create_command(cut_string);
			isFound = true;
		}
		else if (command.contains("DROP TABLE")) {
			//System.out.println("\nDROPTABLE Found command: " + command);
			isFound = true;
		}
		else if (command.contains("OPEN")) {
			//System.out.println("\nOPEN Found command: " + command);
			isFound = true;
		}
		else if (command.contains("CLOSE")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("WRITE")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("EXIT")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("SHOW")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("UPDATE")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("DELETE FROM")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("INSERT INTO")) {
			System.out.println("\nINSERT INTO invoked");
			insert_command(cut_string);
			isFound = true;
		}
		else if (command.contains("SELECT")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("PROJECT")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("RENAME")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("+")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("-")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("*")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("JOIN")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("<-")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else if (command.contains("==") || 
			command.contains("!=") || 
			command.contains("<") || 
			command.contains(">") || 
			command.contains("<=") ||
			command.contains(">=")) {
			//System.out.println("\nFound command: " + command);
			isFound = true;
		}
		else {
			//System.out.println("\nNOT found command: " + command);
		}
		//Table new_table = Engine.createTable("hi", ar, ar);
		return command;
	}

	public static Table create_command(String cut_string){	
		
		String table_name = "";
		String attributes_string = "";
		String keys_string = "";

		// Get the table name
		String pattern_table_name = "^[^\\(]+";
		Pattern r1 = Pattern.compile(pattern_table_name);
		Matcher m1 = r1.matcher(cut_string);
		if (m1.find()) {
			table_name = m1.group(0).trim();
			cut_string = cut_string.replace(m1.group(0), "");
		}

		// Get the attribute list
		String pattern_attribute_list = ".+?(?=PRIMARY KEY)";
		Pattern r2 = Pattern.compile(pattern_attribute_list);
		Matcher m2 = r2.matcher(cut_string);
		if (m2.find()) {
			attributes_string = m2.group(0).trim();
			cut_string = cut_string.replace(m2.group(0), "");
		}

		// Parse the data into an array
		String attribute_list_string = "";
		String[] attributes_array = attributes_string.split(",");
		attributes_array[0] = attributes_array[0].replaceFirst("[\\(]", "");
		attributes_array[attributes_array.length - 1] = attributes_array[attributes_array.length - 1]
			.substring(0,attributes_array[attributes_array.length - 1].length()-1);
		for (int i = 0; i < attributes_array.length; i++){

			// Get the table name
			String pattern_attributes = ".+?(?=INTEGER|VARCHAR)";
			Pattern r21 = Pattern.compile(pattern_attributes);
			Matcher m21 = r21.matcher(attributes_array[i]);
			if (m21.find()) {
				attribute_list_string += m21.group(0).trim() + " ";
			}
		}
		attributes_array = attribute_list_string.split(" ");

		// Get the primary keys
		String pattern_keys_list = "(?<=PRIMARY KEY).+?(?=;)";
		Pattern r3 = Pattern.compile(pattern_keys_list);
		Matcher m3 = r3.matcher(cut_string);
		if (m3.find()) {
			keys_string = m3.group(0).trim();
			cut_string = cut_string.replace(m3.group(0), "");
		}

		// Parse the data into an array
		String[] keys_array = keys_string.split(",");
		keys_array[0] = keys_array[0].replaceFirst("[\\(]", "");
		keys_array[keys_array.length - 1] = keys_array[keys_array.length - 1].substring(0,keys_array[keys_array.length - 1].length()-1);

		System.out.println("Table name: " + table_name);
		System.out.println("Attributes String: " + Arrays.toString(attributes_array));
		System.out.println("Keys String: " + Arrays.toString(keys_array));

		Table new_table = Engine.createTable(table_name.trim(), attributes_array, keys_array);
		return new_table;
	}

	public static void insert_command(String cut_string){
		String table_name = "";
		String data_string = "";

		// Get the table name
		String pattern_table_name = ".+?(?=VALUES FROM)";
		Pattern r1 = Pattern.compile(pattern_table_name);
		Matcher m1 = r1.matcher(cut_string);
		if (m1.find()) {
			table_name = m1.group(0).trim();
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

		System.out.println("Table name: " + table_name);
		System.out.println("Data String: " + Arrays.toString(data_array));
		System.out.println("");

		// Place data in pre-existing table
		Engine.insertRow(table_name.trim(), data_array);
	}

}













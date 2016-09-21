import java.util.*;

public class Engine {

	static HashMap<String, Table> rdbms_tables_container = new HashMap<String, Table>(); //This represents our entire database
	
	public static void main(String[] args){


	}

	public static void create_table(String table_name, String[] keys, String[] p_keys){

		// Check if the table already exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null) {
			// print error message, exit 
		}
		else {
			// Otherwise, create the new table
			// Returns a table initialized with ID and wanted attributes
			Table new_table = new Table(table_name, keys); 


			// Store the created table in the tables container
			rdbms_tables_container.put(table_name, new_table);
		}

	}

	void insert(String table_name, String[] values){

		// get the ArrayList from the rdbms_tables_container, if it exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null) {
			// Print error message and quit
		}
		// NEED TO PUT IN A CASE TO CHECK THE VALUES GIVEN WITH VALUES OF GIVEN TABLE
		else{
			String[] new_values = new String[values.length+1]; // used to put unique id in the new_values[0]
			new_values[0] = values[0] + values[1]; // Creates a new key based on given primary key 
			for(int i = 0; i < values.length; i++){	// rest of the values moved over 1 to the right.
				new_values[i+1] = values[i];
			}
			temp_table.addRow(new_values);
		}

	}

	void show(){

	}
}

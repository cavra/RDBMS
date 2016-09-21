import java.util.*;

public class Engine {

	// needs a class? For storing data such as pk?
	static ArrayList<String, ArrayList<String, String>> rdbms_tables_container = new ArrayList<String, ArrayList<String, String>>();

	static ArrayList<String, String[]> player_list = new ArrayList<String, String[]>();
	static ArrayList<String, String[]> team_list = new ArrayList<String, String[]>();
	static ArrayList<String, String[]> sport_list = new ArrayList<String, String[]>();

	public static void main(String[] args){

	}

	public static void create_table(String table_name, String[] keys, String[] p_keys){

		// Check if the table already exists
		String value = rdbms_tables_container.get(table_name);
		if (value != null) {
			// print error message, exit 
		}
		else {
			// Otherwise, create the new table
			ArrayList<String, String[]> new_table = new ArrayList<String, String[]>();

			// Add the keys as the top-level entry
			new_table.add(keys);

			// Store the created table in the tables container
			rdbms_tables_container.add(table_name, new_table);
		}

	}

	void insert(String table_name, String[] values){

		// get the ArrayList from the rdbms_tables_container, if it exists
		String value = rdbms_tables_container.get(table_name);
		if (value != null) {
			break; 
		}

		table = rdbms_tables_container.get(table_name);

		// Need to make sure the key used here is the primary key
		table.add(values[0] + values [1], values);


	}

	void show(){

	}
}

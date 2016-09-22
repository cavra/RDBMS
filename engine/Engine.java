import java.util.*;

public class Engine {

	static HashMap<String, Table> rdbms_tables_container = new HashMap<String, Table>(); //This represents our entire database
	// This hashmap stores Tables, which are basically arrayLists, which contain vectors

	public static void main(String[] args){

	}

	public static void createTable(String table_name, String[] attributes, String[] p_keys){

		// Check if the table already exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null){
			// print error message, exit
		}
		else {
			// Otherwise, create the new table
			// Returns a table initialized with ID and wanted attributes
			Table new_table = new Table(table_name, attributes, p_keys); 

			// Store the created table in the tables container
			rdbms_tables_container.put(table_name, new_table);
		}
	}

	public static void dropTable(String table_name){
		// Check if the table already exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if(temp_table != null){
			// print error message, exit
		}
		else {
			temp_table.deleteTable();
			rdbms_tables_container.remove(table_name);
		}
	}

	public static void insertRow(String table_name, String[] values){

		// get the ArrayList from the rdbms_tables_container, if it exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null) {
			// Print error message and quit
		}
		// NEED TO PUT IN A CASE TO CHECK THE VALUES GIVEN WITH VALUES OF GIVEN TABLE
		else{
			Vector<String> new_values = new Vector<String>(); // used to put unique id in the new_values[0]

			// Creates a new key based on given primary key given by the user
			String p_key = temp_table.getPKey(values);
			new_values.add(p_key); 

			for(int i = 1; i < values.length + 1; i++){	// rest of the values moved over 1 to the right.
				new_values.add(values[i-1]);
			}
			temp_table.addRow(new_values);
		}
	}

	public static void updateRow(String table_name, String row_id, String[] values){

		// Check if the table and row exist
		Table temp_table = rdbms_tables_container.get(table_name);
		Vector<String> temp_row = temp_table.getRow(row_id);
		if (temp_table != null && temp_row.size() != 0) {
			// Print error message and quit
		}
		else{

			// delete the old row
			deleteRow(table_name, row_id);

			// create a new one
			Vector<String> new_values = new Vector<String>(); // used to put unique id in the new_values[0]
			new_values.add(values[0] + values[1]); // Creates a new key based on given primary key 
			for(int i = 1; i < values.length + 1; i++){	// rest of the values moved over 1 to the right.
				new_values.add(values[i-1]);
			}
			temp_table.addRow(new_values);
		}

	}

	public static void deleteRow(String table_name, String row_id){

		// Check if the table exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null) {
			// Print error message and quit
		}
		else{
			temp_table.deleteRow(row_id);
		}
	}

	public static void show(String table_name){
		// Check if the table exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null) {
			// Print error message and quit
		}
		else{
			temp_table.show();
		}
	}

	public static void projection(){

	}

	public static void setUnion(){

	}

	public static void setDifference(){

	}

	public static void rename(String old_table_name, String new_table_name){
		// Check if the table already exists
		Table temp_table = rdbms_tables_container.get(old_table_name);
		if (temp_table != null){
			// print error message, exit
		}
		else {
			rdbms_tables_container.remove("old_table_name");
			rdbms_tables_container.put(new_table_name, temp_table);
		}
	}

	public static void naturalJoin(){

	}

	public static void write(String table_name){

		// This currently writes the WHOLE database, need to fix it to just write a single table?
        /*File file = new File("info.txt");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(fileObj);
        s.close();*/
	}

	public static void close(String table_name){

	}


}

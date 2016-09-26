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

	public static void naturalJoin(String table1, String[] values1, String table2, String[] values2){ // This function finds commonalities between tables and merges them
		Table temp_table1 = rdbms_tables_container.get(table1); // Creates temporary table for arg1
		Table temp_table2 = rdbms_tables_container.get(table2);	// Creates temporary table for arg2
		int table1_width = temp_table1.attribute_table.get(0).size();
		int table2_width = temp_table2.attribute_table.get(0).size();
		String[] new_values = new String[temp_table1.attribute_table.size() + temp_table2.attribute_table.size()]; // Creates new array for combined attributes
		int new_index = 1; // This allows us to increment both for loops
		for(int i = 1; i < table1_width; i++){ // Puts table1 values in new_values array
			new_values[i] = temp_table1.attribute_table.get(0).get(i);
			new_index = i;

		}
		for(int i = new_index+1; i < (table1_width + table2_width); i++){	// puts table2 values in new_values array AFTER table1 values are inserted
			new_values[i] = temp_table2.attribute_table.get(0).get(i - table1_width);
		}
		Table new_table = new Table((table1+table2), new_values, temp_table1.primary_keys); // Created the new table with combined attributes 
		for(int j = 0; j < temp_table1.attribute_table.size(); j++){ // Analyzes each row of table1 with each row of table2
			Vector<String> temp_row = temp_table1.attribute_table.get(j); // Retrieves each vector of table1
			String temp_pk = temp_row.get(0); // gets the primary id to be compared to the id's of table2
			for(int k = 0; k < temp_table2.attribute_table.size(); k++){ 
				// Looks at the first element of each Vector<String> in ArrayList for the primary key
				Vector<String> temp_row2 = temp_table2.attribute_table.get(k);
				String temp_pk2 = temp_row2.get(0);
				if(temp_pk == temp_pk2){ // Compares each primary key of table1 to table2
					Vector<String> new_vector = new Vector<String>(table1_width+table2_width-1); // new vector to be added to joined table (without the key redundancy)
					for(int l = 0; l < table1_width; l++){
						new_vector.add(temp_row.get(l));
					}
					for(int m = 1; m < table2_width; m++){
						new_vector.add(temp_row2.get(m));
					}
					new_table.addRow(new_vector); // adds combined vector to new table
				}
			}
		}
	}

	
}

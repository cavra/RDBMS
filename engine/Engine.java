import java.util.*;

public class Engine {

	static HashMap<String, Table> rdbms_tables_container = new HashMap<String, Table>(); //This represents our entire database
	// This hashmap stores Tables, which are basically arrayLists, which contain vectors

	public static void main(String[] args){

		TestList new_test_list = new TestList();
		new_test_list.callAll();

	}

	public static void createTable(String table_name, String[] attributes, String[] p_keys){

		// Check if the table already exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table != null){
			System.out.println("Error: Table already exists. Failed to create.");
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
		if(temp_table == null){
			System.out.println("Error: Table doesn't exist. Failed to drop.");
		}
		else {
			System.out.println("Dropped table: " + table_name);
			temp_table.deleteTable();
			rdbms_tables_container.remove(table_name);
		}
	}

	public static void insertRow(String table_name, String[] values){

		// get the table (ArrayList) from the rdbms_tables_container, if it exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table == null) {
			System.out.println("Error: Table doesn't exist, failed to insert row.");
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
		if (temp_table == null && temp_row.size() == 0) {
			System.out.println("Error: Table and/or row don't exist. Failed to update.");
		}
		else{
			temp_table.updateRow(table_name, row_id, values);
		}

	}

	public static void deleteRow(String table_name, String row_id){

		// Check if the table exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table == null) {
			System.out.println("Error: Table doesn't exist. Failed to delete row.");
		}
		else{
			temp_table.deleteRow(row_id);
		}
	}

	public static void show(String table_name){
		// Check if the table exists
		Table temp_table = rdbms_tables_container.get(table_name);
		if (temp_table == null) {
			System.out.println("Error: Cannot show table; table doesn't exist.");
		}
		else{
			temp_table.show();
		}
	}

	//This function takes in two table and combines them w/o saving duplicates
	public static void setUnion(String new_table_name, String table1, String values1, String table2, String values2){
		Table temp_table1 = rdbms_tables_container.get(table1); 
		Table temp_table2 = rdbms_tables_container.get(table2);	
		int table1_width = temp_table1.attribute_table.get(0).size();
		int table2_width = temp_table2.attribute_table.get(0).size();
		int total_width = table1_width+table2_width;
		String[] new_values = new String[temp_table1.attribute_table.size() + temp_table2.attribute_table.size()]; // Creates new array for combined attributes
		int new_index = 1; // This allows us to increment both for loops
		for(int i = 1; i < table1_width ; i++)//Add table1 attributes to array
		{
			new_values[i] = temp_table1.attribute_table.get(0).get(i);
			new_index = i;
		}
		for(int i = new_index; i < total_width ; i++) //Add table2 attributes to array
		{
			new_values[i] = temp_table2.attribute_table.get(0).get(i-table1_width);
		}
		Table union_table = new Table(new_table_name, new_values, temp_table1.primary_keys);//Table of combined attributes
		int index = 1;
		while(index<temp_table1.attribute_table.size())//Adding attributes of table1 to union_table
		{
			union_table.attribute_table.add(temp_table1.attribute_table.get(index));
			index++;
		}
		for(int i = 1; i<temp_table2.attribute_table.size(); i++)//Checking for duplicate attributes and removing them
		{
			for(int j = 1; j<temp_table1.attribute_table.size(); j++)
			{
				if(temp_table2.attribute_table.get(i).get(0) == temp_table1.attribute_table.get(i).get(0))
				{
					continue;
				}
				else
				{
					union_table.attribute_table.add(temp_table2.attribute_table.get(i));
				}
			}	

		}

	}

	//This function takes two tables and subtracts duplicates from the first table
	public static void setDifference(String new_table_name, String table1, String values1, String table2, String values2){
		Table temp_table1 = rdbms_tables_container.get(table1);//Creates temporary table 1
		Table temp_table2 = rdbms_tables_container.get(table2);//Creates temporary table 2
		Table difference_table = new Table(new_table_name, temp_table1.attributes, temp_table1.primary_keys);	
		for(int i = 1; i<temp_table1.attribute_table.size(); i++)
		{
			Vector<String> temp = temp_table1.attribute_table.get(i);
			for(int j = 1;j<temp_table2.attribute_table.size(); j++)
			{
				Vector<String> temp2 = temp_table2.attribute_table.get(i);
				if(temp.get(0) == temp2.get(0))
					continue;
				else
					difference_table.attribute_table.add(temp);
		}
		}
	}

	public static void crossproduct(String table_name1, String table_name2){
		Table table1 = rdbms_tables_container.get(table_name1);
		Table table2 = rdbms_tables_container.get(table_name2);

		// Check that both tables exist
		if (table1 != null && table2 != null){
			// print error message, exit
		}
		else {
			// Create a new table with the combined data
			String cp_table_name = "Cross Product of " + table_name1 + " and " + table_name2;
			List<String> cp_table_attributes = new ArrayList<String>(table1.attributes.length + table2.attributes.length);
		    Collections.addAll(cp_table_attributes, table1.attributes);
		    Collections.addAll(cp_table_attributes, table2.attributes);
		    String[] cp_table_attributes_arr = cp_table_attributes.toArray(new String[cp_table_attributes.size()]);
		    String[] cp_p_keys = table1.primary_keys;

			Table cp_table = new Table(cp_table_name, cp_table_attributes_arr, cp_p_keys);

			// Insert each row combined with each other row
			for (Vector<String> row1 : table1.attribute_table) {
				for (Vector<String> row2 : table2.attribute_table) {
					Vector<String> combined_row = new Vector<String>();
					combined_row.addAll(row1);
					combined_row.addAll(row2);
					cp_table.addRow(combined_row);
				}
			}

			// Store the created table in the tables container
			rdbms_tables_container.put(cp_table_name, cp_table);
		}
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

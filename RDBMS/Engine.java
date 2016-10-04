import java.util.*;
import java.io.*;

public class Engine {

	// This hashmap stores Tables, which are arrayLists containing vectors
	static HashMap<String, Table> rdbms_tables_container = new HashMap<String, Table>(); 

	public static void main(String[] args){
		// Call the test class
		TestList new_test_list = new TestList();
		new_test_list.callAll();
	}

// ==========================================================================================================================
// This function below takes input from the parser and creates essentially an empty table 
// ==========================================================================================================================

	public static Table createTable(String table_name, String[] attributes, String[] p_keys){
		// Check if the table already exists
		Table table = rdbms_tables_container.get(table_name);
		if (table != null){
			System.out.println("Error: Table already exists. Failed to create.");
		}
		else{
			// Returns a table initialized with ID and wanted attributes
			Table new_table = new Table(table_name, attributes, p_keys); 

			// Store the created table in the tables container
			rdbms_tables_container.put(table_name, new_table);

			System.out.println("Created table: " + table_name);
			return new_table;
		}
		return table;
	}
	
// ==========================================================================================================================
// This function below takes in the name of an existing table in the database and removes it.
// Does this by calling a deleteTable function which is self explanatory.
// ==========================================================================================================================

	public static void dropTable(String table_name){
		// Check if the table exists
		Table table = rdbms_tables_container.get(table_name);
		if(table == null){
			System.out.println("Error: Table doesn't exist. Failed to drop.");
		}
		else{
			table.deleteTable();
			rdbms_tables_container.remove(table_name);
			System.out.println("Dropped table: " + table_name);
		}
	}
	
// ==========================================================================================================================
// This function below takes in a table and an array of values. It uses this data to create a 
// Vector<String> row and adds it to the given table.
// ==========================================================================================================================

	public static void insertRow(String table_name, String[] values){
		// Check if the table exists
		Table table = rdbms_tables_container.get(table_name);
		if (table == null){
			System.out.println("Error: Table doesn't exist, failed to insert row.");
		}
		else{
			Vector<String> new_values = new Vector<String>(); // used to put unique id in the new_values[0]

			// Creates a new key based on given primary key given by the user
			String p_key = table.getPKey(values);
			new_values.add(p_key); 

			// Add the rest of the values
			for(int i = 1; i < values.length + 1; i++){
				new_values.add(values[i-1]);
			}
			table.addRow(new_values);
			System.out.println("Inserted row into table: " + table_name);
		}
	}
	
// ==========================================================================================================================
// This function is very straightforward. It checks to see if the given table exists.
// Then, calls the non static updateRow function which is used to change the data.
// ==========================================================================================================================

	public static void updateRow(String table_name, String attribute_type, String attribute, String[] values){
		// Check if the table and row exist
		Table table = rdbms_tables_container.get(table_name);
		String row_id = table.getRowID(attribute_type, attribute);
		if (table == null || row_id == 0){
			System.out.println("Error: Table and/or row don't exist. Failed to update row.");
		}
		else{
			table.updateRow(row_id, values);
			System.out.println("Updated row with key " + row_id + " in table " + table_name);
		}
	}
	
// ==========================================================================================================================
// This function below is very straighforward. It checks to see if the given table exists. 
// If it does, the function then calls the non static deleteRow function to remove that set
// of data.
// ==========================================================================================================================

	public static void deleteRow(String table_name, String attribute_type, String attribute){
		// Check if the table and row exist
		Table table = rdbms_tables_container.get(table_name);
		String row_id = table.getRowID(attribute_type, attribute);
		if (table == null || row_id == 0){
			System.out.println("Error: Table and/or row don't exist. Failed to delete row.");
		}
		else{
			table.deleteRow(row_id);
			System.out.println("Deleted row with key " + row_id + " in table " + table_name);
		}
	}
	
// ===========================================================================================================================
// This function below is very straightforward. It simply, removes the table from the DBMS,
// adds it back with a new key.
// ===========================================================================================================================

	public static void renameTable(String old_table_name, String new_table_name){
		// Check if the table already exists
		Table table = rdbms_tables_container.get(old_table_name);
		if (table == null){
			System.out.println("Error: Cannot rename table; table doesn't exist.");
		}
		else {
			System.out.println("Renaming table");
			table.attribute_table.get(0).set(0, new_table_name);
			rdbms_tables_container.remove(old_table_name);
			rdbms_tables_container.put(new_table_name, table);
			System.out.println("Renamed " + old_table_name + " to " + new_table_name);
		}
	}

// ==========================================================================================================================
// This function below is very straighforward. It first checks to see if the given table exists.
// Then, essentially calls the nonstatic show method which then prints off the data in a given table.
// ==========================================================================================================================

	public static void show(String table_name){
		// Check if the table exists
		Table table = rdbms_tables_container.get(table_name);
		if (table == null) {
			System.out.println("Error: Cannot show table; table doesn't exist.");
		}
		else{
			table.show();
		}
	}
	
// ==========================================================================================================================
// This function is used to return the data that meets a given condition,
// from a given table. 
// ==========================================================================================================================

	public static void selection(String attribute, String operator, String qualificator, String table_name, String new_table_name){
		// Check if the table exists
		Table table = rdbms_tables_container.get(table_name);
		if (table == null) {
			System.out.println("Error: Cannot select from table; table doesn't exist.");
		}
		else{
			Table selection_table = new Table(new_table_name, table.attributes, table.primary_keys);	

			int index = (Arrays.asList(table.attributes).indexOf(attribute)) + 1;
			// if index == -1, exit

			for(int i = 1; i < table.attribute_table.size(); i++) {
				Vector<String> row = table.attribute_table.get(i);

				// Compare the first element of the row (its id) with the given id
				if (checkCondition(operator, row.get(index), qualificator)){
					selection_table.addRow(row);
				}
			}
			rdbms_tables_container.put(new_table_name, selection_table);			
		}
	}
	
// ==========================================================================================================================
// This function below is used to take in an operator as a string and
// return the given operation in executable form.
// ==========================================================================================================================

	public static Boolean checkCondition(String operator, String a, String b){
		if ((a.matches("[0-9]+") && (b.matches("[0-9]+")))){
			switch(operator){
				case ">": 
					return (Integer.parseInt(a) > Integer.parseInt(b));
				case "<": 
					return (Integer.parseInt(a) < Integer.parseInt(b));
				case ">=": 
					return (Integer.parseInt(a) >= Integer.parseInt(b));
				case "<=": 
					return (Integer.parseInt(a) >= Integer.parseInt(b));
				case "==": 
					return a == b;
				case "!=": 
					return a != b;
			}
		}	
		return true;
	}

// ==========================================================================================================================
// This function creates a new table composed of a subset of attributes of a given table
// ==========================================================================================================================

	public static void projection(String table_name, String new_table_name, String[] new_attr){
		// Check if the table exists
		Table table = rdbms_tables_container.get(table_name);
		if (table == null) {
			System.out.println("Error: Cannot project from table; table doesn't exist.");
		}
		else{
			Table projection_table = new Table(new_table_name, new_attr, table.primary_keys);
			Vector<Integer> indicies = getIndices(table, new_attr);

			// Loop through all rows of the table 
			for(int i = 1; i < table.attribute_table.size(); i++){
				Vector<String> row = table.attribute_table.get(i);
				Vector<String> row_projected = new Vector<String>();

				// Add the key
				row_projected.add(row.get(0));

				// Add all values using stored indices
				for(int j : indicies){
					row_projected.add(row.get(j + 1));
				}

				projection_table.addRow(row_projected);
			}
			rdbms_tables_container.put(new_table_name, projection_table);
		}
	}
	
// ==========================================================================================================================
// This is a helper function that returns a vector of indicies (for a given subset
// of attributes) to allow for easy location of data. 
// ==========================================================================================================================

	public static Vector<Integer> getIndices(Table table, String[] new_attr){
		Vector<Integer> indicies_list = new Vector<Integer>();

		// Loop through both attribute lists and look for the duplicates
		for (int i = 0; i < table.attributes.length; i++){
			for (int j = 0; j < new_attr.length; j++){
				if (table.attributes[i] == new_attr[j]) {
					indicies_list.add(i);
				}
			}
		}
		return indicies_list;
	}

// ==========================================================================================================================
// This function below takes in two tables and combines the data while removing duplicates (redundancies).
// ==========================================================================================================================

	public static void setUnion(String new_table_name, String table_name1, String table_name2){
		Table table1 = rdbms_tables_container.get(table_name1); //Get table1
		Table table2 = rdbms_tables_container.get(table_name2); //Get table2
		Table union_table = new Table(new_table_name, table1.attributes, table1.primary_keys);	
		
		// Loop through table 1 and record all non-duplicates
		for(int i = 1; i < table1.attribute_table.size(); i++) {

			Vector<String> temp_row = table1.attribute_table.get(i);

			// Check if the row already exists
			if(union_table.getRow(temp_row.get(0)).size() != 0) {
				//System.out.println(temp_row.get(0) + " was not added");
			}
			else {
				//System.out.println(temp_row.get(0) + " was added");
				union_table.addRow(temp_row);
			}
		}
		// Loop through table 2 and record all non-duplicates
		for(int i = 1; i < table2.attribute_table.size(); i++) {

			Vector<String> temp_row = table2.attribute_table.get(i);

			// Check if the row already exists
			if(union_table.getRow(temp_row.get(0)).size() != 0) {
				//System.out.println(temp_row.get(0) + " was not added");
			}
			else {
				//System.out.println(temp_row.get(0) + " was added");
				union_table.addRow(temp_row);
			}
		}
		rdbms_tables_container.put(new_table_name, union_table);
	}
	
// ==========================================================================================================================
// This function below takes in two tables and removes duplicates from the first parameter in the equation.
// The results are then added to a newly created table.
// ==========================================================================================================================

	public static void setDifference(String new_table_name, String table_name1, String table_name2){
		Table table1 = rdbms_tables_container.get(table_name1); //Get table1
		Table table2 = rdbms_tables_container.get(table_name2); //Get table2
		Table difference_table = new Table(new_table_name, table1.attributes, table1.primary_keys);	
		
		// Loop through the first table and record all unique entries
		for(int i = 1; i < table1.attribute_table.size(); i++) {

			// Check if the row already exists in either table
			Vector<String> temp_row1 = table1.attribute_table.get(i);
			if(table2.getRow(temp_row1.get(0)).size() != 0 || difference_table.getRow(temp_row1.get(0)).size() != 0) {
				//System.out.println(temp_row1.get(0) + " was not added");
			}
			else {
				//System.out.println(temp_row1.get(0) + " was added");
				difference_table.addRow(temp_row1);
			}
		}
		rdbms_tables_container.put(new_table_name, difference_table);
	}
	
// ==========================================================================================================================
// This function below takes in two sets of data and returns every combination of sets, in a newly created table.
// ==========================================================================================================================

	public static void crossProduct(String new_table_name, String table_name1, String table_name2){
		Table table1 = rdbms_tables_container.get(table_name1);
		Table table2 = rdbms_tables_container.get(table_name2);

		// Check that both tables exist
		if (table1 == null|| table2 == null){
			System.out.println("Error: Cannot cross product tables; one doesn't exist.");
		}
		else {
			// Create a new table with the combined data
			List<String> cp_table_attributes = new ArrayList<String>(table1.attributes.length + table2.attributes.length);
		    Collections.addAll(cp_table_attributes, table1.attributes);
		    Collections.addAll(cp_table_attributes, table2.attributes);
		    String[] cp_table_attributes_arr = cp_table_attributes.toArray(new String[cp_table_attributes.size()]);
		    String[] cp_p_keys = table1.primary_keys;

			Table cp_table = new Table(new_table_name, cp_table_attributes_arr, cp_p_keys);

			// Loop through the first and second table and record all combined entries
			for(int i = 1; i < table1.attribute_table.size(); i++) {
				Vector<String> row1 = table1.attribute_table.get(i);
				for(int j = 1; j < table2.attribute_table.size(); j++) {
					Vector<String> row2 = table2.attribute_table.get(j);

					// Copy the vectors so we can change them
					Vector row1_copy = new Vector(row1);
					Vector row2_copy = new Vector(row2);

					// Get the new key
					String new_key = (String)row1_copy.get(0) + (String)row2_copy.get(0);
					row1_copy.set(0, new_key);
					row2_copy.remove(0);

					// Combine their elements
					Vector<String> combined_row = new Vector<String>();
					combined_row.addAll(row1_copy);
					combined_row.addAll(row2_copy);
					cp_table.addRow(combined_row);
				}
			}
			rdbms_tables_container.put(new_table_name, cp_table);
		}
	}
	
// ===========================================================================================================================
// This function below takes two tables and their corresponding values in as input. From there,
// it finds the ID's that the tables' have in common. With that, creates a new table with those common
// entities, making sure to combine the attributes of each table.
// ===========================================================================================================================

	public static void naturalJoin(String new_table_name, String table_name1, String table_name2){ 
		Table table1 = rdbms_tables_container.get(table_name1);
		Table table2 = rdbms_tables_container.get(table_name2);
		int table1_width = table1.attribute_table.get(0).size();
		int table2_width = table2.attribute_table.get(0).size();
		String[] new_values = new String[table1.attributes.length + table2.attributes.length]; 
		
		// Puts table1 values in new_values array
		for(int i = 0; i < table1_width - 1; i++){ 
			new_values[i] = table1.attribute_table.get(0).get(i+1);
		}
		// Puts table2 values in new_values array AFTER table1 values are inserted
		for(int i = 0; i < table2_width - 1; i++){	
			new_values[i + table1_width - 1] = table2.attribute_table.get(0).get(i+1);
		}

		// Create the new table with combined attributes using the p
		Table nj_table = new Table((new_table_name), new_values, table1.primary_keys); 

		// Iterate through all values of both tables and store them in the new table
		for(int j = 1; j < table1.attribute_table.size(); j++){
			Vector<String> row1 = table1.attribute_table.get(j);
			for(int k = 1; k < table2.attribute_table.size(); k++){ 
				Vector<String> row2 = table2.attribute_table.get(k);

				// Compares each primary key of table1 to table2
				if(row1.get(0).equals(row2.get(0))){ 
					// A new vector to be added to the joined table (without the key redundancy)
					Vector<String> new_vector = new Vector<String>(table1_width+table2_width-1);
					
					// Copy the vectors so we can change them
					Vector row1_copy = new Vector(row1);
					Vector row2_copy = new Vector(row2);

					// Get the new key
					String new_key = (String)row1_copy.get(0) + (String)row2_copy.get(0);
					row1_copy.set(0, new_key);
					row2_copy.remove(0);

					// Combine their elements
					Vector<String> combined_row = new Vector<String>();
					combined_row.addAll(row1_copy);
					combined_row.addAll(row2_copy);
					nj_table.addRow(combined_row);
				}
			}
		}
		rdbms_tables_container.put(new_table_name, nj_table);
	}

// ===========================================================================================================================
// The function below writes a table's data to a .ser file to save it
// ===========================================================================================================================

public static void writeTable(String table_name){
		try {
			Table table = rdbms_tables_container.get(table_name);

			// Create the .ser file
			FileOutputStream file_out = new FileOutputStream("table_data/" + table_name + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(file_out);

			// Write to the .ser file
			out.writeObject(table);
			out.close();
			file_out.close();
			System.out.println("Serialized data is saved in table_data/" + table_name + ".ser");
     	}
     	catch(IOException i) {
      		i.printStackTrace();
     	}
  	}
  	
// ==========================================================================================================================
// This function below reads through a file and inputs the data. This is important so that the
// data is not last between sessions.
// ==========================================================================================================================

  	public static void readTable(String table_name){
		try {

			Table read_table = null;

			FileInputStream file_in = new FileInputStream("table_data/" + table_name + ".ser");
			ObjectInputStream in = new ObjectInputStream(file_in);

			// Read in the .ser data to a new Table object
			read_table = (Table)in.readObject(); // warning: [unchecked] unchecked cast
			in.close();
			file_in.close();

			// Store the read-in table in the tables container
			rdbms_tables_container.put(table_name, read_table);
		}
		catch(IOException i) {
			i.printStackTrace();
			return;
		}
		catch(ClassNotFoundException c) {
			System.out.println("Table data not found");
			c.printStackTrace();
			return;
		}
  	}
}

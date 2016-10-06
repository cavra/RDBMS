import java.util.*;
import java.io.*;

public class Engine {

	static HashMap<String, Table> relations_database = new HashMap<String, Table>(); 

	public static void main(String[] args){}

// =============================================================================
// This function below takes input from the parser and creates essentially an 
// empty table 
// =============================================================================

	public static Table createTable(String relation_name, String[] attributes, String[] p_keys){
		// Check if the table already exists
		Table table = relations_database.get(relation_name);
		if (table != null){
			System.out.println("Error: Table already exists. Failed to create.");
			return table;
		}
		else {
			// Returns a table initialized with ID and wanted attributes
			Table new_table = new Table(relation_name, attributes, p_keys); 

			// Store the created table in the tables container
			relations_database.put(relation_name, new_table);

			System.out.println("Created table: " + relation_name);
			return new_table;
		}
	}
	
// =============================================================================
// This function below takes in the name of an existing table in the database 
// and removes it. Does this by calling a deleteTable function
// =============================================================================

	public static void dropTable(String relation_name){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if(table == null){
			System.out.println("Error: Table doesn't exist. Failed to drop.");
		}
		else {
			table.deleteTable();
			relations_database.remove(relation_name);
			System.out.println("Dropped table: " + relation_name);
		}
	}
	
// =============================================================================
// This function below takes in a table and an array of values. It uses this 
// data to create a Vector<String> row and adds it to the given table.
// =============================================================================

	public static void insertRow(String relation_name, String[] values){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
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
		}
	}
	
// =============================================================================
// This function is very straightforward. It checks to see if the given table exists.
// Then, calls the non static updateRow function which is used to change the data.
// =============================================================================

	public static void updateRow(String relation_name, Vector<String> attribute_types, Vector<String> new_attributes, Vector<String> tokenized_conditions){ //Vector<String> conditions_vector
		// Check if the table and row exist
		Table table = relations_database.get(relation_name);
		if (table == null){
			System.out.println("Error: Table and/or row don't exist. Failed to update row.");
		}
		else{
			// Loop through all the rows of a table
			for (Vector<String> row : table.attribute_table) {
				if (parseConditions(table, row, tokenized_conditions)) {
					// Row has met the condition, update it with each value in the new_attributes vector
					// new_attributes and attributes_type have the same size
					for (int i = 0; i < new_attributes.size(); i++) {
						Integer update_attribute_index = table.getAttributeIndex(attribute_types.get(i));
						// Check that the attribute to update exists
						if (update_attribute_index >= 0) {
							table.updateRow(row.get(0), update_attribute_index, new_attributes.get(i));
						}
					}
					break; // We only want to update the first row we find, no more
				}
			}
		}
	}
	
// =============================================================================
// This function below is very straighforward. It checks to see if the given table exists. 
// If it does, the function then calls the non static deleteRow function to remove that set
// of data.
// =============================================================================

	public static void deleteRow(String relation_name, Vector<String> tokenized_conditions){
		// Check if the table and row exist
		Table table = relations_database.get(relation_name);
		if (table == null){
			System.out.println("Error: Table and/or row don't exist. Failed to delete row.");
		}
		else{
			// Loop through all the rows of a table
			for (int i = 0; i < table.attribute_table.size(); i++) {
				Vector<String> row = table.attribute_table.get(i);
				if (parseConditions(table, row, tokenized_conditions)) {
					// Row has met the condition, delete it
					table.deleteRow(row.get(0));
				}
			}
		}
	}
	
// ===========================================================================================================================
// This function below is very straightforward. It simply, removes the table from the DBMS,
// adds it back with a new key.
// ===========================================================================================================================

	public static void renameTable(String old_relation_name, String new_relation_name){
		// Check if the table already exists
		Table table = relations_database.get(old_relation_name);
		if (table == null){
			System.out.println("Error: Cannot rename table; table doesn't exist.");
		}
		else {
			System.out.println("Renaming table");
			table.attribute_table.get(0).set(0, new_relation_name);
			relations_database.remove(old_relation_name);
			relations_database.put(new_relation_name, table);
			System.out.println("Renamed " + old_relation_name + " to " + new_relation_name);
		}
	}

// =============================================================================
// This function below is very straighforward. It first checks to see if the given table exists.
// Then, essentially calls the nonstatic show method which then prints off the data in a given table.
// =============================================================================

	public static void show(String relation_name){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Cannot show table; table doesn't exist.");
		}
		else{
			table.show();
		}
	}
	
// =============================================================================
// This function is used to return the data that meets a given condition,
// from a given table. 
// =============================================================================

	public static Table selection(String relation_name, Vector<String> tokenized_conditions){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Cannot select from table; table doesn't exist.");
			return null;
		}
		else{
			Table selection_table = new Table("Selection from " + relation_name, table.attributes, table.primary_keys);	

			// Loop through all the rows of a table
			for (int i = 1; i < table.attribute_table.size(); i++) {
				Vector<String> row = table.attribute_table.get(i);
				if (parseConditions(table, row, tokenized_conditions)) {
					// Row has met the condition, add it to the selection table
					selection_table.addRow(row);
				}
			}
			return selection_table;		
		}
	}

// =============================================================================
// This function creates a new table composed of a subset of attributes of a given table
// =============================================================================

	public static Table projection(String relation_name, Vector<String> new_attributes_vector){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Cannot project from table; table doesn't exist.");
			return null;
		}
		else {
			String[] new_attributes_array = new_attributes_vector.toArray(new String[new_attributes_vector.size()]);
			Table projection_table = new Table("Projection from " + relation_name, new_attributes_array, table.primary_keys);
			Vector<Integer> indicies = getIndices(table, new_attributes_array);

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
			return projection_table;
		}
	}

// =============================================================================
// =============================================================================

	public static Table rename(String relation_name, Vector<String> new_attributes_vector){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Cannot project from table; table doesn't exist.");
			return null;
		}
		else {
			String[] new_attributes_array = new_attributes_vector.toArray(new String[new_attributes_vector.size()]);

			String[] new_p_keys = new String[table.p_key_indices.size()];
			for (int i = 0; i < table.p_key_indices.size(); i++) {
				new_p_keys[i] = new_attributes_array[table.p_key_indices.get(i)];
			}

			Table rename_table = new Table("Renamed from " + relation_name, new_attributes_array, new_p_keys);

			// Loop through all rows of the table 
			for(int i = 1; i < table.attribute_table.size(); i++){
				Vector<String> row = table.attribute_table.get(i);
				rename_table.addRow(row);
			}
			return rename_table;
		}
	}
// =============================================================================
// This function below takes in two tables and combines the data while removing duplicates (redundancies).
// =============================================================================

	public static Table setUnion(String new_relation_name, String relation_name1, String relation_name2){
		Table table1 = relations_database.get(relation_name1); //Get table1
		Table table2 = relations_database.get(relation_name2); //Get table2
		Table union_table = new Table(new_relation_name, table1.attributes, table1.primary_keys);	
		
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
		relations_database.put(new_relation_name, union_table);
		return union_table;
	}
	
// =============================================================================
// This function below takes in two tables and removes duplicates from the first parameter in the equation.
// The results are then added to a newly created table.
// =============================================================================

	public static Table setDifference(String new_relation_name, String relation_name1, String relation_name2){
		Table table1 = relations_database.get(relation_name1); //Get table1
		Table table2 = relations_database.get(relation_name2); //Get table2
		Table difference_table = new Table(new_relation_name, table1.attributes, table1.primary_keys);	
		
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
		relations_database.put(new_relation_name, difference_table);
		return difference_table;
	}
	
// =============================================================================
// This function below takes in two sets of data and returns every combination of sets, in a newly created table.
// =============================================================================

	public static Table crossProduct(String new_relation_name, String relation_name1, String relation_name2){
		Table table1 = relations_database.get(relation_name1);
		Table table2 = relations_database.get(relation_name2);

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

			Table cp_table = new Table(new_relation_name, cp_table_attributes_arr, cp_p_keys);

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
			relations_database.put(new_relation_name, cp_table);
			return cp_table;
		}
		return null;
	}
	
// ===========================================================================================================================
// This function below takes two tables and their corresponding values in as input. From there,
// it finds the ID's that the tables' have in common. With that, creates a new table with those common
// entities, making sure to combine the attributes of each table.
// ===========================================================================================================================

	public static Table naturalJoin(String new_relation_name, String relation_name1, String relation_name2){ 
		Table table1 = relations_database.get(relation_name1);
		Table table2 = relations_database.get(relation_name2);
		int table1_width = table1.attribute_table.get(0).size();
		int table2_width = table2.attribute_table.get(0).size();
		Vector<String> new_values = new Vector<String>(table1.attributes.length + table2.attributes.length); 
		
		// Puts table1 values in new_values array
		for(int i = 0; i < table1_width - 1; i++){ 
			new_values.add(table1.attribute_table.get(0).get(i+1));
		}
		// Puts table2 values in new_values array AFTER table1 values are inserted
		for(int i = 0; i < table2_width - 1; i++){	
			new_values.add(table2.attribute_table.get(0).get(i+1));
		}

		for(int i = 0; i < new_values.size(); i++){
			for(int p = 0; p < new_values.size(); p++){
				if(i!=p){
                  if(new_values.elementAt(i).equals(new_values.elementAt(p))){
                     new_values.removeElementAt(p);
                  }
                }
			}
		}

		String[] new_values_array = new_values.toArray(new String[new_values.size()]);


		// Create the new table with combined attributes using the p
		Table nj_table = new Table((new_relation_name), new_values_array, table1.primary_keys); 

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

					for(int i = 0; i < combined_row.size(); i++)
					{
						for(int p = 0; p < combined_row.size(); p++)
						{
							if(i!=p)
                    		{
                        		if(combined_row.elementAt(i).equals(combined_row.elementAt(p)))
                        		{
                        			combined_row.removeElementAt(p);
                        		}
                   		 	}
						}
					}
					nj_table.addRow(combined_row);
				}
			}
		}
		relations_database.put(new_relation_name, nj_table);
		return nj_table;
	}


// =============================================================================
// This function below reads through a file and inputs the data. This is important so that the
// data is not last between sessions.
// =============================================================================

  	public static void openTable(String relation_name){
		try {

			Table read_table = null;

			FileInputStream file_in = new FileInputStream("table_data/" + relation_name + ".ser");
			ObjectInputStream in = new ObjectInputStream(file_in);

			// Read in the .ser data to a new Table object
			read_table = (Table)in.readObject(); // warning: [unchecked] unchecked cast
			in.close();
			file_in.close();

			// Store the read-in table in the tables container
			relations_database.put(relation_name, read_table);
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

// ===========================================================================================================================
// The function below writes a table's data to a .ser file to save it
// ===========================================================================================================================

	public static void writeTable(String relation_name){
		try {
			// Check that the table exists
			Table table = relations_database.get(relation_name);
			if (table == null){
				System.out.println("Error: Cannot cross write table; table doesn't exist.");
			}
			else {
				// Create the .ser file
				FileOutputStream file_out = new FileOutputStream("table_data/" + relation_name + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(file_out);

				// Write to the .ser file
				out.writeObject(table);
				out.close();
				file_out.close();
				System.out.println("Serialized data is saved in table_data/" + relation_name + ".ser");
			}
     	}
     	catch(IOException i) {
      		i.printStackTrace();
     	}
  	}

	
// =============================================================================
// This function below takes in the name of an existing table in the database 
// and removes it. Does this by calling a deleteTable function
// =============================================================================

  	// Currently, this is the same as dropTable! Fix this, check piazza for difference
	public static void closeTable(String relation_name){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if(table == null){
			System.out.println("Error: Table doesn't exist. Failed to drop.");
		}
		else{
			table.deleteTable();
			relations_database.remove(relation_name);
			System.out.println("Dropped table: " + relation_name);
		}
	}

// =============================================================================
// This is a helper function that returns a vector of indicies (for a given subset
// of attributes) to allow for easy location of data. 
// =============================================================================

	public static Vector<Integer> getIndices(Table table, String[] new_attr){
		Vector<Integer> indicies_list = new Vector<Integer>();

		// Loop through both attribute lists and look for the duplicates
		for (int i = 0; i < table.attributes.length; i++){
			for (int j = 0; j < new_attr.length; j++){
				if (table.attributes[i].equals(new_attr[j])) {
					indicies_list.add(i);
				}
			}
		}
		return indicies_list;
	}

// =============================================================================
// This helper function takes in a given row and a tokenized vector of 
// conditions. From then, it will apply each condition to the row and return
// true if a row meets all the conditions
// =============================================================================

	public static Boolean parseConditions(Table table, Vector<String> row, Vector<String> token_vector){
		// Stores 1 complete comparison
		// e.g. "kind", "==", "dog"
		Vector<String> comparison_vector = new Vector<String>();
		Boolean value = false; // default to false

		for (int i = 0; i < token_vector.size(); i++){
			// The end of the comparison has been reached
			if (token_vector.get(i).equals(";") || 
				token_vector.get(i).equals(")")) {
				value = evaluateCondition(table, row, comparison_vector);
				break;
			}
			else if (i == token_vector.size() - 1) {
				comparison_vector.add(token_vector.get(i));
				value = evaluateCondition(table, row, comparison_vector);	
				break;			
			}
			// Handle the && operator
			else if (token_vector.get(i).equals("&&")) {
				Vector<String> and_comparison_vector = new Vector<String>();
				for (int j = i+1; j < token_vector.size(); j++){
					and_comparison_vector.add(token_vector.get(j));
				}
				value = (evaluateCondition(table, row, comparison_vector) && parseConditions(table, row, and_comparison_vector));
				break;
			}
			// Handle the || operator
			else if (token_vector.get(i).equals("||")) {
				Vector<String> or_comparison_vector = new Vector<String>();
				for (int j = i+1; j < token_vector.size(); j++){
					or_comparison_vector.add(token_vector.get(j));
				}
				value = (evaluateCondition(table, row, comparison_vector) || parseConditions(table, row, or_comparison_vector));
				break;
			}
			// Handle nested comparisons
			else if(token_vector.get(i).equals("(")){
				Vector<String> nested_comparison = new Vector<String>();
				for (int j = i+1; j < token_vector.size(); j++){
					nested_comparison.add(token_vector.get(j));
				}
				value = parseConditions(table, row, nested_comparison);
			}
			else {
				comparison_vector.add(token_vector.get(i));
			}
		}
		return value;
	}

// =============================================================================
// =============================================================================

	public static Boolean evaluateCondition(Table table, Vector<String> row, Vector<String> condition_vector){
		Integer attribute1_index = table.getAttributeIndex(condition_vector.firstElement()); // kind -> 2
		Integer attribute2_index = table.getAttributeIndex(condition_vector.lastElement()); // akind -> 2
		String attribute1 = "";
		String attribute2 = "";
		Boolean value = false; // default to false

		if (attribute1_index < 0) {
			attribute1 = condition_vector.get(0);
		}
		else {
			attribute1 = row.get(attribute1_index);
		}

		String operator = condition_vector.get(1);

		if (attribute2_index < 0) {
			attribute2 = condition_vector.get(2);
		}
		else {
			attribute2 = row.get(attribute2_index);
		}

		// If the attribute exists, and the row meets the condition...
		if (checkCondition(attribute1, operator, attribute2)){
			value = true;
		}
		return value;
	}

// =============================================================================
// =============================================================================

	public static Boolean checkCondition(String attribute1, String operator, String attribute2){
		String integer_regex = "[0-9]+";
		Boolean value = false; // default to false

		// INTEGER case
		if ((attribute1.matches(integer_regex)) && (attribute2.matches(integer_regex))){
			switch(operator){
				case ">": 
					value = (Integer.parseInt(attribute1) > Integer.parseInt(attribute2));
				case "<": 
					value = (Integer.parseInt(attribute1) < Integer.parseInt(attribute2));
				case ">=": 
					value = (Integer.parseInt(attribute1) >= Integer.parseInt(attribute2));
				case "<=": 
					value = (Integer.parseInt(attribute1) >= Integer.parseInt(attribute2));
				case "==": 
					value = attribute1.equals(attribute2);
				case "!=": 
					value = attribute1 != attribute2;
			}
		}
		// VARCHAR case
		else {
			switch(operator){
				case "==": 
					value = Objects.equals(attribute1, attribute2);
				case "!=": 
					value = !Objects.equals(attribute1, attribute2);
			}
		}
		return value;
	}

	public static Boolean tableExists(String relation_name) {
		Table table = relations_database.get(relation_name);
		if (table == null){
			System.out.println("Error: Table and/or row don't exist.");
			return false;
		}
		else {
			return true;
		}
	}

}

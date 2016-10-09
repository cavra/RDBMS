import java.util.*;
import java.io.*;

public class Engine {

	static HashMap<String, Table> relations_database = new HashMap<String, Table>(); 

	public static void main(String[] args) {}

// =============================================================================
// A function to create a new relation and add it to the database
// Parameters: 
//   relation_name: The user-defined relation name
//   attributes: An array of Attributes, which have a name and domain
//   keys: An array of attribute names to determine how a key is formed
// =============================================================================

	public static Table createTable(String relation_name, ArrayList<Attribute> attributes, String[] keys) {
		// Check if the table already exists
		Table table = relations_database.get(relation_name);
		if (table != null) {
			System.out.println("Error: Table already exists. Failed to create.");
			return table;
		}
		else {
			// Returns a table initialized with ID and specified attributes
			Table new_table = new Table(relation_name, attributes, keys);

			// Store the created table in the tables container
			relations_database.put(relation_name, new_table);

			System.out.println("Created table: " + relation_name);
			return new_table;
		}
	}
	
// =============================================================================
// A function to remove an existing relation from the database 
// Parameters:
//   relation_name: The name of the relation to be dropped
// =============================================================================

	public static void dropTable(String relation_name) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; Failed to drop.");
		}
		else {
			table.deleteTable();
			relations_database.remove(relation_name);
			System.out.println("Dropped table: " + relation_name);
		}
	}

// =============================================================================
// A function to add a new row to an existing table
// Parameters:
//   relation_name: The name of the relation accepting the new row
//   values: A String Array of values that will make up the row
// =============================================================================

	public static void insertRow(String relation_name, ArrayList<String> values) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to insert row.");
		}
		else {
			Row row = new Row(values, table.makeKey(values));

			// Only insert if all values are valid and the key isn't empty
			if (table.isRowValid(values) && !row.key.equals("")) {
				table.addRow(row);
			}
			// Otherwise, print error message
			else {
				System.out.println("Error: Invalid value detected; failed to insert row.");				
			}
		}
	}

// =============================================================================
// A function to update an existing row
// Parameters:
//   relation_name: The name of the relation the row belongs to
//	 attributes: The attribute columns to update
//	 values: The new attribute values. This ArrayList corresponds with the
//	   attributes ArrayList
//	 tokenized_conditions: The conditions a row must meet
// =============================================================================

	public static void updateRow(String relation_name, ArrayList<String> attributes, ArrayList<String> values, ArrayList<String> tokenized_conditions) { 
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to update row.");
		}
		else {
			// Loop through all the rows of the table
			for (Row row : table) {

				// Check if it meets the required conditions
				if (parseConditions(table, row, tokenized_conditions)) {

					// And only update if all values are valid
					if (table.isRowValid(values)) {

						// Check that the attribute to update exists
						for (int i = 0; i < values.size(); i++) {
							Integer update_attribute_index = table.getAttributeIndex(attributes.get(i));
							if (update_attribute_index >= 0) {

								// The row meets all conditions, update it
								table.updateRow(row.key, update_attribute_index, values.get(i));
							}
						}
					}
				}
			}
		}
	}
	
// =============================================================================
// A function to delete an existing row
// Parameters:
//   relation_name: The name of the relation the row belongs to
//	 tokenized_conditions: The conditions a row must meet in order to be deleted
// =============================================================================

	public static void deleteRow(String relation_name, ArrayList<String> tokenized_conditions) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to delete row.");
		}
		else {
			// Loop through all the rows of the table
			for (Row row : table) {

				// Check if it meets the required conditions
				if (parseConditions(table, row, tokenized_conditions)) {

					// The row meets all conditions, delete it
					table.deleteRow(row.get(0));
				}
			}
		}
	}

// =============================================================================
// A function show an existing relation
// Parameters: 
//   relation_name: The name of the relation to show
// =============================================================================

	public static void show(String relation_name) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to show.");
		}
		else {
			table.show();
		}
	}
	
// =============================================================================
// A function to return a new table from a selection
// Parameters: 
//   relation_name: The name of the relation to perform the selection on
//	 tokenized_conditions: The conditions a row must meet in order to be 
//     selected
// =============================================================================

	public static Table selection(String relation_name, ArrayList<String> tokenized_conditions) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to select.");
			return null;
		}
		else {
			Table selection_table = new Table("Selection from " + relation_name, table.attributes, table.keys);	

			// Loop through all the rows of the table
			for (Row row : table) {

				// Check if it meets the required conditions
				if (parseConditions(table, row, tokenized_conditions)) {

					// Row has met the condition, add it to the selection table
					selection_table.addRow(row);
				}
			}
			return selection_table;		
		}
	}

// =============================================================================
// A function to return a new table from a projection
// Parameters: 
//   relation_name: The name of the relation to perform the projection on
//	 attributes: The list of attributes to keep from the original table
// =============================================================================

	public static Table projection(String relation_name, ArrayList<Attribute> attributes) {
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to project.");
			return null;
		}
		else {
			Table projection_table = new Table("Projection from " + relation_name, attributes, table.keys);

			// Get the indices of the new attribute columns from the original table
			ArrayList<Integer> indicies = new ArrayList<Integer>();
			for (Attribute attribute : attributes) {
				int i = table.getAttributeIndex(attribute.name);
				if (i < 0) {
					indicies.add(i);
				}
			}

			// Loop through all the rows of the table
			for (Row original_row : table) {

				// Get the specified attributes values using obtained indices
				ArrayList<String> projected_values = new ArrayList<String>();
				for(int i : indicies){
					projected_values.add(original_row.values.get(i));
				}

				// Create a new row with the new data
				Row projected_row = new Row(projected_values, original_row.key);

				// And add it to the projected table
				projection_table.addRow(projected_row);
			}
			return projection_table;
		}
	}

// =============================================================================
// A function to return a new table from with renamed attributes
// Parameters: 
//   relation_name: The name of the relation with attributes to rename
//	 attributes: The list of new, renamed attributes to apply to the relation
// =============================================================================

	public static Table rename(String relation_name, ArrayList<String> attributes){
		// Check if the table exists
		Table table = relations_database.get(relation_name);
		if (table == null) {
			System.out.println("Error: Table doesn't exist; failed to rename.");
			return null;
		}
		else {
			// Get a copy of the current attributes
			ArrayList<Attribute> new_attributes = table.attributes;
			// and rename them with the corresponding new attributes
			for (int i = 0; i < new_attributes.length; i++) {
				new_attributes.get(i).rename(attributes.get(i));
			}

			// Get an updated primary key ArrayList using the original table's stored indices
			ArrayList<Integer> new_keys = new ArrayList<Integer>();
			for (int i = 0; i < table.key_indices.size(); i++) {
				new_keys.add(new_attributes[table.key_indices.get(i)].name);
			}

			Table rename_table = new Table("Renamed from " + relation_name, new_attributes, new_keys);

			// Add all the original rows to the new table
			for(int i = 1; i < table.attribute_table.size(); i++){
				ArrayList<String> row = table.attribute_table.get(i);
				rename_table.addRow(row);
			}
			return rename_table;
		}
	}
// =============================================================================
// A function to return a new table, created by taking the union of two tables
// Parameters: 
//   relation_name1: The relation name of the first table
//   relation_name2: The relation name of the second table
// =============================================================================

	public static Table setUnion(String relation_name1, String relation_name2){
		Table table1 = relations_database.get(relation_name1);
		Table table2 = relations_database.get(relation_name2);
		String union_relation_name = "Set Union from " + relation_name1 + " and " + relation_name2;
		Table union_table = new Table(union_relation_name, table1.attributes, table1.keys);	
		
		// Loop through table 1 and record all non-duplicate rows
		for (Row row : table1) {

			// Check if the row already exists
			if (union_table.getRow(row.key) != null) {
				union_table.addRow(row);
			}
		}

		// Loop through table 2 and record all non-duplicate rows
		for (Row row : table2) {

			// Check if the row already exists
			if (union_table.getRow(row.key) != null) {
				union_table.addRow(row);
			}
		}

		relations_database.put(new_relation_name, union_table);
		return union_table;
	}
	
// =============================================================================
// A function to return a new table, created by taking the difference of two 
//   tables
// Parameters: 
//   relation_name1: The relation name of the first table
//   relation_name2: The relation name of the second table
// =============================================================================

	public static Table setDifference(String relation_name1, String relation_name2){
		Table table1 = relations_database.get(relation_name1);
		Table table2 = relations_database.get(relation_name2);
		String union_relation_name = "Set Difference from " + relation_name1 + " and " + relation_name2;
		Table difference_table = new Table(new_relation_name, table1.attributes, table1.keys);	
		
		// Loop through the first table and record all unique entries
		for(int i = 1; i < table1.attribute_table.size(); i++) {

			// Check if the row already exists in either table
			ArrayList<String> temp_row1 = table1.attribute_table.get(i);
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
			List<Attribute> cp_table_attributes = new ArrayList<Attribute>(table1.attributes.length + table2.attributes.length);
		    Collections.addAll(cp_table_attributes, table1.attributes);
		    Collections.addAll(cp_table_attributes, table2.attributes);
		    Attribute[] cp_table_attributes_arr = cp_table_attributes.toArray(new Attribute[cp_table_attributes.size()]);
		    String[] cp_p_keys = table1.keys;

			Table cp_table = new Table(new_relation_name, cp_table_attributes_arr, cp_p_keys);

			// Loop through the first and second table and record all combined entries
			for(int i = 1; i < table1.attribute_table.size(); i++) {
				ArrayList<String> row1 = table1.attribute_table.get(i);
				for(int j = 1; j < table2.attribute_table.size(); j++) {
					ArrayList<String> row2 = table2.attribute_table.get(j);

					// Copy the ArrayLists so we can change them
					ArrayList row1_copy = new ArrayList(row1);
					ArrayList row2_copy = new ArrayList(row2);

					// Get the new key
					String new_key = (String)row1_copy.get(0) + (String)row2_copy.get(0);
					row1_copy.set(0, new_key);
					row2_copy.remove(0);

					// Combine their elements
					ArrayList<String> combined_row = new ArrayList<String>();
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
	
// =============================================================================
// This function below takes two tables and their corresponding values in as input. From there,
// it finds the ID's that the tables' have in common. With that, creates a new table with those common
// entities, making sure to combine the attributes of each table.
// =============================================================================

	public static Table naturalJoin(String new_relation_name, String relation_name1, String relation_name2){ 
		Table table1 = relations_database.get(relation_name1);
		Table table2 = relations_database.get(relation_name2);
		int table1_width = table1.attribute_table.get(0).size();
		int table2_width = table2.attribute_table.get(0).size();
		ArrayList<String> new_values = new ArrayList<String>(table1.attributes.length + table2.attributes.length); 
		
		// Put table1 values in new_values array
		for(int i = 0; i < table1_width - 1; i++){ 
			new_values.add(table1.attribute_table.get(0).get(i+1));
		}
		// Put table2 values in new_values array after table1's values are inserted
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

		Attribute[] new_values_array = new_values.toArray(new Attribute[new_values.size()]);

		// Create the new table with combined attributes using the p
		Table nj_table = new Table((new_relation_name), new_values_array, table1.keys); 

		// Iterate through all values of both tables and store them in the new table
		for(int j = 1; j < table1.attribute_table.size(); j++){
			ArrayList<String> row1 = table1.attribute_table.get(j);
			for(int k = 1; k < table2.attribute_table.size(); k++){ 
				ArrayList<String> row2 = table2.attribute_table.get(k);

				// Compares each primary key of table1 to table2
				if(row1.get(0).equals(row2.get(0))){ 
					// A new ArrayList to be added to the joined table (without the key redundancy)
					ArrayList<String> new_ArrayList = new ArrayList<String>(table1_width+table2_width-1);
					
					// Copy the ArrayLists so we can change them
					ArrayList row1_copy = new ArrayList(row1);
					ArrayList row2_copy = new ArrayList(row2);

					// Get the new key
					String new_key = (String)row1_copy.get(0) + (String)row2_copy.get(0);
					row1_copy.set(0, new_key);
					row2_copy.remove(0);

					// Combine their elements
					ArrayList<String> combined_row = new ArrayList<String>();
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

// =============================================================================
// The function below writes a table's data to a .ser file to save it
// =============================================================================

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
// This helper function takes in a given row and a tokenized ArrayList of 
// conditions. From then, it will apply each condition to the row and return
// true if a row meets all the conditions
// =============================================================================

	public static Boolean parseConditions(Table table, ArrayList<String> row, ArrayList<String> token_ArrayList){
		// Stores 1 complete comparison
		// e.g. "kind", "==", "dog"
		ArrayList<String> comparison_ArrayList = new ArrayList<String>();
		Boolean value = false; // default to false

		for (int i = 0; i < token_ArrayList.size(); i++){
			// The end of the comparison has been reached
			if (token_ArrayList.get(i).equals(";") || 
				token_ArrayList.get(i).equals(")")) {
				value = evaluateCondition(table, row, comparison_ArrayList);
				break;
			}
			else if (i == token_ArrayList.size() - 1) {
				comparison_ArrayList.add(token_ArrayList.get(i));
				value = evaluateCondition(table, row, comparison_ArrayList);	
				break;			
			}
			// Handle the && operator
			else if (token_ArrayList.get(i).equals("&&")) {
				ArrayList<String> and_comparison_ArrayList = new ArrayList<String>();
				for (int j = i+1; j < token_ArrayList.size(); j++){
					and_comparison_ArrayList.add(token_ArrayList.get(j));
				}
				value = (evaluateCondition(table, row, comparison_ArrayList) && parseConditions(table, row, and_comparison_ArrayList));
				break;
			}
			// Handle the || operator
			else if (token_ArrayList.get(i).equals("||")) {
				ArrayList<String> or_comparison_ArrayList = new ArrayList<String>();
				for (int j = i+1; j < token_ArrayList.size(); j++){
					or_comparison_ArrayList.add(token_ArrayList.get(j));
				}
				value = (evaluateCondition(table, row, comparison_ArrayList) || parseConditions(table, row, or_comparison_ArrayList));
				break;
			}
			// Handle nested comparisons
			else if(token_ArrayList.get(i).equals("(")){
				ArrayList<String> nested_comparison = new ArrayList<String>();
				for (int j = i+1; j < token_ArrayList.size(); j++){
					nested_comparison.add(token_ArrayList.get(j));
				}
				value = parseConditions(table, row, nested_comparison);
			}
			else {
				comparison_ArrayList.add(token_ArrayList.get(i));
			}
		}
		return value;
	}

// =============================================================================
// =============================================================================

	public static Boolean evaluateCondition(Table table, ArrayList<String> row, ArrayList<String> condition_ArrayList){
		Integer attribute1_index = table.getAttributeIndex(condition_ArrayList.firstElement()); // kind -> 2
		Integer attribute2_index = table.getAttributeIndex(condition_ArrayList.lastElement()); // akind -> 2
		String attribute1 = "";
		String attribute2 = "";
		Boolean value = false; // default to false

		if (attribute1_index < 0) {
			attribute1 = condition_ArrayList.get(0);
		}
		else {
			attribute1 = row.get(attribute1_index);
		}

		String operator = condition_ArrayList.get(1);

		if (attribute2_index < 0) {
			attribute2 = condition_ArrayList.get(2);
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

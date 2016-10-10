import java.util.*;
import java.io.*;

public class Table implements Serializable {
	
	// Global Variables
	public ArrayList<Row> relation = new ArrayList<Row>();
	public String relation_name;
	public ArrayList<Attribute> attributes;
	public ArrayList<String> keys;
	public ArrayList<Integer> key_indices = new ArrayList<Integer>();

// =============================================================================
// The Table constructor
// Parameters:
//   relation_name: The user-defined relation name
//   attributes: An ArrayList of Attributes, which have a name and domain
//   keys: An ArrayList of attribute names to determine how a key is formed
// =============================================================================

	Table(String relation_name, ArrayList<Attribute> attributes, ArrayList<String> keys) {
		// Set the class variables
		this.relation_name = relation_name;
		this.attributes = attributes;
		this.keys = keys;

		// Record the indexes of the primary keys in the attribute list
		for (int i = 0; i < attributes.size(); i++) {
			for (int j = 0; j < keys.size(); j++) {
				if (attributes.get(i).name.equals(keys.get(j))) {
					key_indices.add(i);
				}
			}
		}
	}

// =============================================================================
// The table destructor
//   A function to delete a table by setting all Relation data to null
//   Java's garbage collector takes care of the rest automatically
// =============================================================================

	public void deleteTable() {
		relation = null;
		key_indices = null;
		relation_name = null;
		attributes = null;
		keys = null;
	}

// =============================================================================
// A function that adds a row to a table
// Parameters:
//   new_row: A row of attribute values to be added
// =============================================================================

	public void addRow(Row new_row) {
		if (new_row.size() != 0) {
			relation.add(new_row);
			System.out.println("Inserted row with key " + new_row.key + " in table " + relation_name);
		}
		else {
			System.out.println("Error: Row with id " + new_row.key + " in table " + relation_name + " doesn't exist; failed to insert");			
		}
	}

// =============================================================================
// A function that deletes a row from a table
// Parameters:
//   row_id: The primary key of the row to be deleted
// =============================================================================

	public void deleteRow(String row_id) {
		Row row = getRow(row_id);
		if (row != null) {
			relation.remove(row);
			System.out.println("Deleted row with key " + row_id + " in table " + relation_name);
		}
		else {
			System.out.println("Error: Row with id " + row_id + " in table " + relation_name + " doesn't exist; failed to delete");			
		}
	}

// =============================================================================
// A function that updates a row in a table
// Parameters: 
//   row_id: The primary key of the row to be deleted
//   attribute_index: The index corresponding to a column in a table
//   new_attribute: the new attribute to replace the one at the old index
// =============================================================================

	public void updateRow(String row_id, Integer attribute_index, String new_attribute) {
		// Get the row, if it exists
		Row row = getRow(row_id);
		if (row != null) {
			row.values.set(attribute_index, new_attribute);
			System.out.println("Updated row with key " + row_id + " in table " + relation_name);
		}
		else {
			System.out.println("Error: Row with id " + row_id + " in table " + relation_name + " doesn't exist; failed to update");			
		}
	}
	
// =============================================================================
// A function to print a relation
// =============================================================================

	public void show() {
		// Print the header
		System.out.print("Key");
		for(int i = 0; i < attributes.size(); i++) {
			System.out.print(" " + attributes.get(i).name);
		}

		System.out.print("\n");

		// Print each row
		for (int i = 0; i < relation.size(); i++) {
			Row row = relation.get(i);
			System.out.println(row.key + ": " + row.values);
		}
	}

// =============================================================================
// A function to create a row's primary key from an array of Attribute values
//   using the stored key indices
// Parameters:
//   attribute_values: The ArrayList of Attribute values to extract the key from
// =============================================================================

	public String createKey(ArrayList<String> attribute_values) {
		String key = "";

		// Concatenate the key with all elements of the primary keys
		for (Integer i : key_indices) {
			key += attribute_values.get(i);
		}
		return key.replaceAll("\"", "");
	}

// =============================================================================
// A function to return a row based on a row_id
// Parameters:
//   row_id: The primary key of a row, which is always the first element of the 
//     row ArrayList
// =============================================================================

	public Row getRow(String row_id) {
		// Search each row in the table
		for (Row row : relation) {

			// Compare the first element of the row (its id) with the given id
			if (row.key.equals(row_id)){
				return row;
			}
		}

		System.out.println("Error: Row doesn't exist. Cannot get.");
		return null;
	}

// =============================================================================
// A function to return a row ID from an attribute value and column
// Parameters:
//   attribute_value: The value to search for. ("Dog" or "13")
//   attribute_name: The attribute, or column, to search inside of. 
//     e.g. "Kind" or "Age"
// =============================================================================

	public String getRowID(String attribute_value, String attribute_name){
		Integer p_index = 0;
		String row_id = "0";

		// Get the index for the desired attribute column
		for (int i = 0; i < attributes.size(); i++){
			if (attributes.get(i).name.equals(attribute_name)) {
				p_index = i;
			}
		}

		// Using the column index, search each row for a match
		for (Row row : relation) {
			if (row.values.get(p_index).equals(attribute_value)){
				row_id = row.key;
			}
		}
		return row_id;
	}

// =============================================================================
// A function to get the index of a given attribute 
// Parameters:
//   attribute_name: The attribute, or column, to search 
//     e.g. "Kind" or "Age"
// =============================================================================

	public Integer getAttributeIndex(String attribute_name) {
		Integer index = -1;

		// Search the first row for the desired index
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).name.equals(attribute_name)) {
				index = i;
 			}
		}

		// Print error if attribute not found
		if (index == -1) {
			//System.out.println("Error: " + attribute_name + " doesn't exist in table's attributes. Cannot get index.");
		}
		return index;
	}

// =============================================================================
// A function to determine if a row's values match their respective domain
// Parameters:
//   attribute_name: The attribute, or column, to search inside of. 
//     e.g. "Kind" or "Age"
// =============================================================================

	public Boolean isRowValid(Row row) {
		Boolean all_values_are_valid = true;

		// The row is either missing a value or contains too many
		if (attributes.size() != row.size()) {
			all_values_are_valid = false;
		}

		// Compare each value to its respective attribute domain
		for (int i = 0; i < attributes.size(); i++) {

			// If just one value is invalid, set this to false
			if (!attributes.get(i).isValid(row.values.get(i))) {
				all_values_are_valid = false;
			}
		}
		return all_values_are_valid;
	}
}
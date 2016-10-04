import java.util.*;
import java.io.*;

public class Table implements Serializable{
	
	ArrayList<Vector<String>> attribute_table = new ArrayList<Vector<String>>();
	Vector<Integer> p_key_indices = new Vector<Integer>();
	String[] attributes;
	String[] primary_keys;
	String relation_name;

// ==========================================================================================================================
// The function below is the constructor for the Table Class. It assigns each table object a:
// attribute table (holds all data within the table), primary keys indices vector
// (holds the indicies of each attribute used to identity each entity), an array of the given
// attributes, an array of the primary key's used by each corresponding entity, nad lastly a
// table name.
// ==========================================================================================================================

	Table(String r_name, String[] attribute_list, String[] p_keys){		// Take an array of attribute names and insert as first row in arraylist
		relation_name = r_name;
		attributes = attribute_list.clone();
		primary_keys = p_keys.clone();

		// Record the indexes of the primary keys in the attribute list
		for (int i = 0; i < attribute_list.length; i++){
			for (int j = 0; j < p_keys.length; j++){
				if (attribute_list[i].equals(p_keys[j])) {
					p_key_indices.add(i);
				}
			}
		}

		// The first element of the first array will always be "Key"
		Vector<String> keys_vector = new Vector<String>();
		keys_vector.add("Key");

		// Copy all the attributes to the vector
		for(int i = 0; i < attribute_list.length; i++) {
			keys_vector.add(attribute_list[i]);
		}

		// Set the first row 
		attribute_table.add(keys_vector);
	}
	
// ==========================================================================================================================
// This function below is very simple. It essentially just sets the table to NULL. 
// This approach was used because the java garbage collector will come and remove 
// all unreferenced data.
// ==========================================================================================================================

	public void deleteTable(){
		// Java's garbage collector will take care of the rest
		attribute_table = null;
	}

// ==========================================================================================================================
// This function below essentially just adds the specified row to the attribute table (member).
// ==========================================================================================================================

	public void addRow(Vector<String> new_row){
		attribute_table.add(new_row);
		System.out.println("Inserted row with key " + new_row.get(0) + " in table " + relation_name);
	}
	
// ==========================================================================================================================
// This function below checks to see if the row exists and calls the built in remove function
// implemented in the ArrayList class.
// ==========================================================================================================================

	public void deleteRow(String row_id){
		Vector<String> row = getRow(row_id);
			attribute_table.remove(row);
		System.out.println("Deleted row with key " + row_id + " in table " + relation_name);
	}

// ==========================================================================================================================
// This function below first checks to see if the data exists. Next, it uses the values
// parameter to create a new row with the updated values.
// ==========================================================================================================================

	public void updateRow(String row_id, Integer attribute_index, String new_attribute){
		// Get the row, if it exists
		Vector<String> row = getRow(row_id);
		row.set(attribute_index, new_attribute);
		System.out.println("Updated row with key " + row_id + " in table " + relation_name);
	}
	
// ==========================================================================================================================
// This function below simply iterates through the given tables and prints out
// all of the data within the attribute table.
// ==========================================================================================================================

	public void show(){
		// Iterate through each row
		for (Vector<String> row : attribute_table) {
			System.out.println(row);
		}
	}

// ==========================================================================================================================
// This function below searches the table for a row. If found, it is returned.
// Otherwise, it returns an empty Vector<String>.
// ==========================================================================================================================

	public Vector<String> getRow(String row_id){ //row_id is just the first element of the specified row
		// Search each row in the table
		for (Vector<String> row : attribute_table) {
			// Compare the first element of the row (its id) with the given id
			if (row.firstElement().equals(row_id)){
				return row;
			}
		}

		// If the row was not found, return an empty array
		Vector<String> empty_row = new Vector<String>(0);
		//System.out.println("Error: Row doesn't exist. Cannot get.");
		return empty_row;
	}

// ==========================================================================================================================
// This function below takes in an attribute type and an attribute. From then it 
// will return the row ID, or Row key
// ==========================================================================================================================

	public String getRowID(String attribute_type, String attribute){
		// Set the initial string as empty
		Integer p_index = 0;

		for (int i = 0; i < attributes.length; i++){
			if (attributes[i].equals(attribute_type)) {
				p_index = i;
			}
		}

		// Iterate and concatenate it with all elements of the array
		for (Vector<String> row : attribute_table) {
			if (row.get(p_index).equals(attribute)){
				return row.get(0);
			}
		}
		return "0";
	}

// ==========================================================================================================================
// This function below takes in a string of values and uses the primary key indices to
// find the data needed to create a unique primary key.
// ==========================================================================================================================

	public String getPKey(String[] values){
		// Set the initial string as empty
		String p_key = "";

		// Iterate and concatenate it with all elements of the array
		for (Integer i : p_key_indices) {
			p_key += values[i];
		}
		return p_key;
	}
	
// ==========================================================================================================================
// This function below searches the table for a row. If found, it is returned.
// Otherwise, it returns an empty Vector<String>.
// ==========================================================================================================================

	public Integer getAttributeIndex(String attribute_type){ //row_id is just the first element of the specified row
		// Search the first row for the desired index
		Integer index = -1;

		for (int i = 0; i < attributes.length; i++){
			if (attributes[i].equals(attribute_type)) {
				index = i + 1;
 			}
		}

		if (index == -1) {
			System.out.println("Error: Attribute doesn't exist. Cannot get index.");
		}

		return index;
	}

}
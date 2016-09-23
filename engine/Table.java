import java.util.*;

public class Table{
	
	ArrayList<Vector<String>> attribute_table = new ArrayList<Vector<String>>();
	String unique_id;
	Vector<Integer> row_keys;
	String[] attributes;
	String[] primary_keys;

	Table(String t_name, String[] attribute_list, String[] p_keys){		// Take an array of attribute names and insert as first row in arraylist

		attributes = attribute_list.clone();
		primary_keys = p_keys.clone();

		String[] new_attribute_list = new String[attribute_list.length + 1];

		// Record the indexes of the primary keys in the attribute list
		for (int i = 0; i < attribute_list.length; i++){
			for (int j = 0; j < p_keys.length; j++){
				if (attribute_list[i] == p_keys[j]) {
					row_keys.add(i);
				}
			}
		}

		// leave this empty
		new_attribute_list[0] = t_name;

		// Move all other values over
		for(int i = 0; i < attribute_list.length; i++){
			new_attribute_list[i + 1] = attribute_list[i];  // array = {table ID, rest of attributes...}
		}


	}

	public void deleteTable(){
		attribute_table = null;
	}

	public String getPKey(String[] values){

		String p_key = "";

		for (Integer index : row_keys) {
			p_key += values[index];
		}

		return p_key;
	}

	public void addRow(Vector<String> new_row){
		attribute_table.add(new_row);
	}

	public void deleteRow(String row_id){
		
		// Get the row, if it exists
		Vector<String> row = getRow(row_id);
		if (row.size() != 0){
			attribute_table.remove(row);
		}
		else{
			// row does not exist, exit
		}
	}

	// Searches the table for a row, returns empty array if not found
	public Vector<String> getRow(String row_id){ //row_id is just the first element of the specified row
		
		// Search each row in the table
		for (Vector<String> row : attribute_table) {

			// Compare the first element of the row (its id) with the given id
			if (row.get(0) == row_id){
				return row;
			}
		}

		// If the row was not found, return an empty array
		Vector<String> empty_row = new Vector<String>(0);
		return empty_row;
	}

	public void show(){
		for (int i = 0; i < attribute_table.size(); i++){
			for (int j = 0; j < attribute_table.get(0).size(); j++){
				System.out.print(j + "\t");
			}
			System.out.println("");
		}
	}

	public void selection(String attribute, String operator, String qualificator, String table_name, String new_table_name){
		//i = attributes.indexOf(attribute);
		// if i == -1, exit

		//for (Vector<String> row : attribute_table) {

			// Compare the first element of the row (its id) with the given id
			//if (getOp(operator, row.get(i), qualificator){
				// add it to a new table
			//}
		//}
	}

	public Boolean getOp(String operator, String a, String b){
		//if ((a.matches("[0-9]+") && (b.matches("[0-9]+")))){
/*
			switch(operator)
				case ">": 
					return (a > b);
					break;
				case "<": 
					return (a < b);
					break;
				case ">=": 
					return a >= b;
					break;
				case "<=": 
					return a >= b;
					break;
				case "==": 
					return a == b;
					break;
				case "!=": 
					return a != b;
					break;
		//}	*/
		return true;
	}
}







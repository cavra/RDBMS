import java.util.*;

public class Table{
	
	ArrayList<String[]> attribute_table = new ArrayList<String[]>();
	String unique_id;

	Table(String t_name, String[] attribute_list){		// Take an array of attribute names and insert as first row in arraylist
		unique_id = t_name; //the table name is unique in itself
		String[] new_attribute_list = new String[attribute_list.length+1];
		new_attribute_list[0] = unique_id;
		for(int i = 0; i < attribute_list.length; i++){
			new_attribute_list[i+1] = attribute_list[i];  // array = {table ID, rest of attributes...}
		}
	}

	public void deleteTable(){
		attribute_table = null;
	}

	public void addRow(String[] new_row){
		attribute_table.add(new_row);
	}

	public void deleteRow(String row_id){
		
		// Get the row, if it exists
		String[] row = getRow(row_id);
		if (row.length != 0){
			attribute_table.remove(row);
		}
		else{
			// row does not exist, exit
		}
	}

	// Searches the table for a row, returns empty array if not found
	public String[] getRow(String row_id){ //row_id is just the first element of the specified row
		
		// Search each row in the table
		for (String[] row : attribute_table) {

			// Compare the first element of the row (its id) with the given id
			if (row[0] == row_id){ 
				return row;
			}
		}

		// If the row was not found, return an empty array
		String[] empty_row = new String[0];
		return empty_row;
	}

}
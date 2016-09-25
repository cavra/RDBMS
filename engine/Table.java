import java.util.*;
import java.io.*;

public class Table implements Serializable{
	
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

		// leave the first index as table name
		new_attribute_list[0] = t_name;

		// Move all other values over
		Vector<String> keys_vector = new Vector<String>();
		for(int i = 0; i < attribute_list.length; i++) {
			new_attribute_list[i + 1] = attribute_list[i];  // array = {table ID, rest of attributes...}
			keys_vector.add(new_attribute_list[i+1]);
		}

		// Set the first row 
		attribute_table.add(keys_vector);

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

	public void writeTable(String table_name){
		try {
			FileOutputStream file_out = new FileOutputStream("table_data/" + table_name + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(file_out);

			out.writeObject(attribute_table);
			out.close();
			file_out.close();
			System.out.printf("Serialized data is saved in table_data/" + table_name + ".ser");
     	}
     	catch(IOException i) {
      		i.printStackTrace();
     	}
  	}

  	public void readTable(String table_name){
		try {
			FileInputStream file_in = new FileInputStream("table_data/" + table_name + ".ser");
			ObjectInputStream in = new ObjectInputStream(file_in);

			ArrayList<Vector<String>> read_table = new ArrayList<Vector<String>>();
			read_table = (ArrayList<Vector<String>>) in.readObject(); // warning: [unchecked] unchecked cast
			in.close();
			file_in.close();
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







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

	public void addRow(String[] new_row){
		attribute_table.add(new_row);
	}

	public void deleteTable(){
		attribute_table = null;
	}

}
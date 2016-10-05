import java.util.*;
import java.io.*;

public class Commands {

	public static Table createCommand(Vector<String> token_vector) {	

		String relation_name = "";
		Vector<String> attribtues_vector = new Vector<String>();
		Vector<String> keys_vector = new Vector<String>();

		// Start the token index at the begining of the relation name
		Integer token_index = 2;

		// Get the relation name
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("(")) {
				token_index = i;
				break;
			}
			else {
				relation_name += token_vector.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		// Get the attribute list
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("PRIMARY")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("VARCHAR")) {
				i = i + 2;
				continue;
			}
			else if (token_vector.get(i).equals("INTEGER")) {
				continue;
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				attribtues_vector.add(token_vector.get(i));
			}
			else {
				System.out.println("Skipping token... " + token_vector.get(i));
			}
		}

		// Get the primary keys list
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("PRIMARY") || token_vector.get(i).equals("KEY")) {
				continue;
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				keys_vector.add(token_vector.get(i));
			}			
			else {
				System.out.println("Skipping token... " + token_vector.get(i));				
			}
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute List:" + attribtues_vector);
		System.out.println("Primary keys List:" + keys_vector);

		// Convert the arrays to vectors
		String[] attributes_array = attribtues_vector.toArray(new String[attribtues_vector.size()]);
		String[] keys_array = keys_vector.toArray(new String[keys_vector.size()]);

		Table new_table = Engine.createTable(relation_name.trim(), attributes_array, keys_array);
		return new_table;
	}

	public static void dropCommand(Vector<String> token_vector) {	
		String relation_name = Grammar.getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.dropTable(relation_name.trim());
	}

	public static void insertCommand(Vector<String> token_vector) {
		String relation_name = "";
		Vector<String> data_vector = new Vector<String>();

		Integer token_index = 2;

		// Gets the relation name
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("VALUES")) {
				token_index = i + 2;
				break;
			}
			else {
				relation_name += token_vector.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		// Get the list of values to be inserted
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("RELATION")) {
				token_index = i;
				Vector<String> new_vec = new Vector<String>();
				for(int j = token_index; j < token_vector.size(); j++) {
					new_vec.add(token_vector.get(j));
				}
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				data_vector.add(token_vector.get(i));
			}
			else {
				System.out.println("Skipping token... " + token_vector.get(i));
			}
		}

		String[] data_array = data_vector.toArray(new String[data_vector.size()]);
		Engine.insertRow(relation_name.trim(), data_array);
	}

	public static void updateCommand(Vector<String> token_vector) {
		String relation_name = "";
		Vector<String> attribute_type_vector = new Vector<String>();
		Vector<String> new_attribute_vector = new Vector<String>();
		Vector<String> condition_vector = new Vector<String>();

		Integer token_index = 1;
		// Get the relation name
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("SET")) {
				token_index = i;
				break;
			}
			else {
				relation_name += token_vector.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		// Get the set of data
		Integer old_token_index = token_index;
		for (int i = old_token_index; i < token_vector.size() - 1; i++) {
			if (token_vector.get(i).equals("WHERE")) {
				token_index = i + 1;
				break;
			}
			else if (token_vector.get(i+1).equals("=")) {
				attribute_type_vector.add(token_vector.get(i));
				new_attribute_vector.add(token_vector.get(i+2));
			}
			else {
				continue;
			}
		}

		for (int i = token_index; i < token_vector.size(); i++) {
			condition_vector.add(token_vector.get(i));
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute Type List:" + attribute_type_vector);
		System.out.println("New Attribute List:" + new_attribute_vector);
		System.out.println("Conditions List:" + condition_vector);

		Engine.updateRow(relation_name, attribute_type_vector, new_attribute_vector, condition_vector);
	}

	public static void deleteCommand(Vector<String> token_vector) {
		String relation_name = "";
		Vector<String> condition_vector = new Vector<String>();

		Integer token_index = 2;
		// Get the relation name
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("WHERE")) {
				token_index = i + 1;
				break;
			}
			else {
				relation_name += token_vector.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		for (int i = token_index; i < token_vector.size(); i++) {
			condition_vector.add(token_vector.get(i));
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Conditions List:" + condition_vector);

		Engine.deleteRow(relation_name, condition_vector);
	}

	public static void showCommand(Vector<String> token_vector) {
		String relation_name = Grammar.getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.show(relation_name.trim());
	}

	public static void openCommand(Vector<String> token_vector) {	
		String relation_name = Grammar.getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.openTable(relation_name.trim()); // Close table???
	}

	public static void writeCommand(Vector<String> token_vector) {
		String relation_name = Grammar.getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.writeTable(relation_name.trim());
	}

	public static void closeCommand(Vector<String> token_vector) {	
		String relation_name = Grammar.getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.closeTable(relation_name.trim());
	}

	public static void exitCommand() {
		System.out.println("Program ending");
		// end program
	}
}
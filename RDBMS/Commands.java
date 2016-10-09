import java.util.*;
import java.io.*;

public class Commands {

	public static Table createCommand(Vector<String> token_vector) {	
		Vector<Attribute> attributes = new Vector<Attribute>();
		Vector<String> attributes_vector = new Vector<String>();
		Vector<String> domains_vector = new Vector<String>();
		Vector<String> keys_vector = new Vector<String>();

		// Start the token index at the begining of the relation name
		Integer token_index = 2;

		// Get the new relation name
		String relation_name = "";
		Vector<String> relation_name_vector = Grammar.retrieveTokens(token_vector, token_index, "(", false);
		for (String string : relation_name_vector) {
			relation_name += string + " ";
		}
		relation_name = relation_name.trim();
		token_index += relation_name_vector.size() + 1;

		// Get the attribute list and their types
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("PRIMARY")) {
				token_index = i;
				break;
			}
			else if (token_vector.get(i).equals("VARCHAR")) {
				domains_vector.add(token_vector.get(i) + " " + token_vector.get(i+2));
				i = i + 2;
				continue;
			}
			else if (token_vector.get(i).equals("INTEGER")) {
				domains_vector.add(token_vector.get(i));
				continue;
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				attributes_vector.add(token_vector.get(i));
			}
			else {
				//System.out.println("Skipping token... " + token_vector.get(i));
			}
		}

		// Create the Attributes
		for (int i = 0; i < attributes_vector.size(); i++) {
			Attribute attribute = new Attribute(attributes_vector.get(i), domains_vector.get(i));
			attributes.add(attribute);
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
				//System.out.println("Skipping token... " + token_vector.get(i));				
			}
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute List:" + attributes_vector);
		System.out.println("Types List:" + domains_vector);
		System.out.println("Primary keys List:" + keys_vector);

		// Convert the arrays to vectors
		Attribute[] attributes_array = attributes.toArray(new Attribute[attributes.size()]);
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
		Vector<String> expression_vector = new Vector<String>();

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

		if (token_vector.get(token_index).equals("(")) {
			// Get the list of values to be inserted
			for (int i = token_index; i < token_vector.size(); i++) {
				if (token_vector.get(i).equals(";")) {
					token_index = i;
					break;
				}
				else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
					expression_vector.add(token_vector.get(i));
				}
				else {
					//System.out.println("Skipping token... " + token_vector.get(i));
				}
			}

			Engine.insertRow(relation_name.trim(), expression_vector);
		}
		else if (token_vector.get(token_index).equals("RELATION")) {
			for (int i = token_index; i < token_vector.size(); i++) {
				if (token_vector.get(i).equals(";")) {
					break;
				}
				else {
					expression_vector.add(token_vector.get(i));
				}
			}
			Table expression_table = Grammar.evaluateExpression(expression_vector);
			for (int i = 1; i < expression_table.attribute_table.size(); i++) {

				// Get the row from the table and insert it
				Vector<String> row = expression_table.attribute_table.get(i);
				Engine.insertRow(relation_name.trim(), row);
			}
		}
	}

	public static void updateCommand(Vector<String> token_vector) {
		String relation_name = "";
		Vector<String> attributes = new Vector<String>();
		Vector<String> values = new Vector<String>();
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
				attributes.add(token_vector.get(i));
				values.add(token_vector.get(i+2));
			}
			else {
				continue;
			}
		}

		for (int i = token_index; i < token_vector.size(); i++) {
			condition_vector.add(token_vector.get(i));
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute List:" + attributes);
		System.out.println("Values List:" + values);
		System.out.println("Conditions List:" + condition_vector);

		Engine.updateRow(relation_name, attributes, values, condition_vector);
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
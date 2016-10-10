import java.util.*;
import java.io.*;

public class Commands {

	public static Table createCommand(ArrayList<String> token_ArrayList) {	
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		ArrayList<String> attribute_names = new ArrayList<String>();
		ArrayList<String> attribute_domains = new ArrayList<String>();
		ArrayList<String> primary_keys = new ArrayList<String>();

		// Start the token index at the begining of the relation name
		Integer token_index = Grammar.skipToToken(token_ArrayList, 0, "table") + 1;

		// Get the new relation name
		String relation_name = "";
		ArrayList<String> relation_name_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, "(", false);
		for (String string : relation_name_ArrayList) {
			relation_name += string + " ";
		}
		relation_name = relation_name.trim();
		token_index += relation_name_ArrayList.size() + 1;

		// Get the attribute list and their types
		for (int i = token_index; i < token_ArrayList.size(); i++) {
			if (token_ArrayList.get(i).equals("PRIMARY")) {
				token_index = i;
				break;
			}
			else if (token_ArrayList.get(i).equals("VARCHAR")) {
				attribute_domains.add(token_ArrayList.get(i) + " " + token_ArrayList.get(i+2));
				i = i + 2;
				continue;
			}
			else if (token_ArrayList.get(i).equals("INTEGER")) {
				attribute_domains.add(token_ArrayList.get(i));
				continue;
			}
			else if (!token_ArrayList.get(i).equals("(") && !token_ArrayList.get(i).equals(")")) {
				attribute_names.add(token_ArrayList.get(i));
			}
			else {
				//System.out.println("Skipping token... " + token_ArrayList.get(i));
			}
		}

		// Create the Attributes
		for (int i = 0; i < attribute_names.size(); i++) {
			Attribute attribute = new Attribute(attribute_names.get(i), attribute_domains.get(i));
			attributes.add(attribute);
		}

		// Get the primary keys list
		for (int i = token_index; i < token_ArrayList.size(); i++) {
			if (token_ArrayList.get(i).equals(";")) {
				token_index = i;
				break;
			}
			else if (token_ArrayList.get(i).equals("PRIMARY") || token_ArrayList.get(i).equals("KEY")) {
				continue;
			}
			else if (!token_ArrayList.get(i).equals("(") && !token_ArrayList.get(i).equals(")")) {
				primary_keys.add(token_ArrayList.get(i));
			}			
			else {
				//System.out.println("Skipping token... " + token_ArrayList.get(i));				
			}
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute Names:" + attribute_names);
		System.out.println("Types List:" + attribute_domains);
		System.out.println("Primary keys List:" + primary_keys);

		Table new_table = Engine.createTable(relation_name, attributes, primary_keys);
		return new_table;
	}

	public static void dropCommand(ArrayList<String> token_ArrayList) {	
		String relation_name = Grammar.getRelationName(token_ArrayList);
		System.out.println("Table Name: " + relation_name);
		Engine.dropTable(relation_name.trim());
	}

	public static void insertCommand(ArrayList<String> token_ArrayList) {
		String relation_name = "";
		ArrayList<String> expression_ArrayList = new ArrayList<String>();

		Integer token_index = 2;

		// Gets the relation name
		for (int i = token_index; i < token_ArrayList.size(); i++) {
			if (token_ArrayList.get(i).equals("VALUES")) {
				token_index = i + 2;
				break;
			}
			else {
				relation_name += token_ArrayList.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		if (token_ArrayList.get(token_index).equals("(")) {
			// Get the list of values to be inserted
			for (int i = token_index; i < token_ArrayList.size(); i++) {
				if (token_ArrayList.get(i).equals(";")) {
					token_index = i;
					break;
				}
				else if (!token_ArrayList.get(i).equals("(") && !token_ArrayList.get(i).equals(")")) {
					expression_ArrayList.add(token_ArrayList.get(i));
				}
				else {
					//System.out.println("Skipping token... " + token_ArrayList.get(i));
				}
			}

			Engine.insertRow(relation_name, expression_ArrayList);
		}
		else if (token_ArrayList.get(token_index).equals("RELATION")) {
			for (int i = token_index; i < token_ArrayList.size(); i++) {
				if (token_ArrayList.get(i).equals(";")) {
					break;
				}
				else {
					expression_ArrayList.add(token_ArrayList.get(i));
				}
			}
			Table expression_table = Grammar.evaluateExpression(expression_ArrayList);
			for (int i = 0; i < expression_table.relation.size(); i++) {

				// Get the row from the table and insert it
				Row row = expression_table.relation.get(i);
				Engine.insertRow(relation_name, row.values);
			}
		}
	}

	public static void updateCommand(ArrayList<String> token_ArrayList) {
		String relation_name = "";
		ArrayList<String> attributes = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> condition_ArrayList = new ArrayList<String>();

		Integer token_index = 1;
		// Get the relation name
		for (int i = token_index; i < token_ArrayList.size(); i++) {
			if (token_ArrayList.get(i).equals("SET")) {
				token_index = i;
				break;
			}
			else {
				relation_name += token_ArrayList.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		// Get the set of data
		Integer old_token_index = token_index;
		for (int i = old_token_index; i < token_ArrayList.size() - 1; i++) {
			if (token_ArrayList.get(i).equals("WHERE")) {
				token_index = i + 1;
				break;
			}
			else if (token_ArrayList.get(i+1).equals("=")) {
				attributes.add(token_ArrayList.get(i));
				values.add(token_ArrayList.get(i+2));
			}
			else {
				continue;
			}
		}

		for (int i = token_index; i < token_ArrayList.size(); i++) {
			condition_ArrayList.add(token_ArrayList.get(i));
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Attribute List:" + attributes);
		System.out.println("Values List:" + values);
		System.out.println("Conditions List:" + condition_ArrayList);

		Engine.updateRow(relation_name, attributes, values, condition_ArrayList);
	}

	public static void deleteCommand(ArrayList<String> token_ArrayList) {
		String relation_name = "";
		ArrayList<String> condition_ArrayList = new ArrayList<String>();

		Integer token_index = 2;
		// Get the relation name
		for (int i = token_index; i < token_ArrayList.size(); i++) {
			if (token_ArrayList.get(i).equals("WHERE")) {
				token_index = i + 1;
				break;
			}
			else {
				relation_name += token_ArrayList.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		for (int i = token_index; i < token_ArrayList.size(); i++) {
			condition_ArrayList.add(token_ArrayList.get(i));
		}

		System.out.println("Table name:" + relation_name);
		System.out.println("Conditions List:" + condition_ArrayList);

		Engine.deleteRow(relation_name, condition_ArrayList);
	}

	public static void showCommand(ArrayList<String> token_ArrayList) {
		String relation_name = Grammar.getRelationName(token_ArrayList);
		System.out.println("Table Name: " + relation_name);
		Engine.show(relation_name.trim());
	}

	public static void openCommand(ArrayList<String> token_ArrayList) {	
		String relation_name = Grammar.getRelationName(token_ArrayList);
		System.out.println("Table Name: " + relation_name);
		Engine.openTable(relation_name.trim()); // Close table???
	}

	public static void writeCommand(ArrayList<String> token_ArrayList) {
		String relation_name = Grammar.getRelationName(token_ArrayList);
		System.out.println("Table Name: " + relation_name);
		Engine.writeTable(relation_name.trim());
	}

	public static void closeCommand(ArrayList<String> token_ArrayList) {	
		String relation_name = Grammar.getRelationName(token_ArrayList);
		System.out.println("Table Name: " + relation_name);
		Engine.closeTable(relation_name.trim());
	}

	public static void exitCommand() {
		System.out.println("Program ending");
		// end program
	}
}
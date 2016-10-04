import java.util.*;
import java.io.*;

public class Grammar {
   
	private Vector<String> token_vector = new Vector<String>();

	Grammar(String line) {

		// Tokenize the input and store in a vector
		String delimiters = "() {};= \t\n\r\f";
		StringTokenizer st = new StringTokenizer(line, delimiters, true);
		while(st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.trim().isEmpty() && !token.equals(",")) {
				token = token.replaceAll(",", "");
				token_vector.add(token);
			 }
		}

		// Reconnect all double operators
		String[] operators = {"!", "<", ">", "="};
		for (int i = 1; i < token_vector.size(); i++) {
			for (String operator: operators) {
				if (token_vector.get(i-1).equals(operator) && token_vector.get(i).equals("=")) {
					token_vector.set(i-1, operator + "=");
					token_vector.remove(i);
				}
			}
		}

		System.out.println("\nTokenized vector: " + token_vector);

		tokenloop:
		for (String token : token_vector) {
			switch(token) {
				// QUERIES
				case "<-":
					System.out.println("QUERY invoked");
					queryQuery(token_vector);
					break tokenloop;
				case "select":
					System.out.println("SELECT invoked");
					selectQuery(token_vector);
					break tokenloop;
				case "project":
					System.out.println("PROJECT invoked");
					projectQuery(token_vector);
					break tokenloop;
				case "rename":
					System.out.println("RENAME invoked");
					break tokenloop;
				case "+":
					System.out.println("SET UNION invoked");
					break tokenloop;
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					break tokenloop;
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					break tokenloop;
				case "JOIN":
					System.out.println("NATURAL JOIN invoked");
					break tokenloop;
				// COMMANDS
				case "CREATE":
					System.out.println("CREATE TABLE invoked");
					createCommand(token_vector);
					break tokenloop;
				case "DROP":
					System.out.println("DROP TABLE invoked");
					dropCommand(token_vector);
					break tokenloop;
				case "INSERT":
					System.out.println("INSERT invoked");
					insertCommand(token_vector);
					break tokenloop;
				case "UPDATE":
					System.out.println("UPDATE ROW invoked");
					updateCommand(token_vector);
					break tokenloop;
				case "DELETE":
					System.out.println("DELETE ROW invoked");
					deleteCommand(token_vector);
					break tokenloop;
				case "SHOW":
					System.out.println("SHOW invoked");
					showCommand(token_vector);
					break tokenloop;
				case "OPEN":
					System.out.println("OPEN invoked");
					openCommand(token_vector);
					break tokenloop;
				case "WRITE":
					System.out.println("OPEN invoked");
					writeCommand(token_vector);
					break tokenloop;
				case "CLOSE":
					System.out.println("CLOSE invoked");
					closeCommand(token_vector);
					break tokenloop;
				case "EXIT":
					System.out.println("EXIT invoked");
					exitCommand();
					break tokenloop;
			}
		}
	}

	public static Table evaluateExpression(Vector<String> token_vector) { //eventually return table
		for (String token : token_vector) {
			switch(token) {
				case "select":
					System.out.println("SELECT invoked");
					return selectQuery(token_vector);
				case "project":
					System.out.println("PROJECT invoked");
					return projectQuery(token_vector);
				case "rename":
					System.out.println("RENAME invoked");
					return renameQuery(token_vector);
				case "+":
					System.out.println("SET UNION invoked");
					return setUnionQuery(token_vector);
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					return setDifferenceQuery(token_vector);
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					return crossProductQuery(token_vector);
				case "JOIN":
					System.out.println("NATURAL JOIN invoked");
					return naturalJoinQuery(token_vector);
				// case "(":
				// 	System.out.println("NESTED EXPRESSION detected");
				// 	return evaluateExpression( naturalJoinQuery(token_vector);
			}
		}
		return null;
	}
 
	public static String getRelationName(Vector<String> token_vector) {
		String relation_name = "";

		if (token_vector.contains("CREATE") ||
			token_vector.contains("SELECT") ||
			token_vector.contains("PROJECT") ||
			token_vector.contains("+") ||
			token_vector.contains("-") ||
			token_vector.contains("*") ||
			token_vector.contains("JOIN")) {
				System.out.println("Nested table detected in getRelationName");
				System.out.println("(Need to handle this!!!)");
		}
		else {
			for (String token : token_vector) {
				if (token.equals("OPEN") ||
					token.equals("CLOSE") ||
					token.equals("WRITE") ||
					token.equals("SHOW") ||
					token.equals("DROP") ||
					token.equals("TABLE")) {
					continue;
				}
				else if (token.equals(";") ||
					token.equals("<-")) {
					break;
				}
				else {
					relation_name += token + " ";					
				}
			}
		}
		relation_name = relation_name.trim();
		return relation_name;
	}

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
				//System.out.println("Skipping token... " + token_vector.get(i));
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
				//System.out.println("Skipping token... " + token_vector.get(i));				
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
		String relation_name = getRelationName(token_vector);
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
				System.out.println(new_vec);
			}
			else if (!token_vector.get(i).equals("(") && !token_vector.get(i).equals(")")) {
				data_vector.add(token_vector.get(i));
			}
			else {
				//System.out.println("Skipping token... " + token_vector.get(i));
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
		String relation_name = getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.show(relation_name.trim());
	}

	public static void openCommand(Vector<String> token_vector) {	
		String relation_name = getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.openTable(relation_name.trim()); // Close table???
	}

	public static void writeCommand(Vector<String> token_vector) {
		String relation_name = getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.writeTable(relation_name.trim());
	}

	public static void closeCommand(Vector<String> token_vector) {	
		String relation_name = getRelationName(token_vector);
		System.out.println("Table Name: " + relation_name);
		Engine.closeTable(relation_name.trim());
	}

	public static void exitCommand() {
		System.out.println("Program ending");
		// end program
	}

	public static void queryQuery(Vector<String> token_vector) {
		Vector<String> expression_vector = new Vector<String>();
		String relation_name = "";
		Integer token_index = 0;

		// Get the new relation name
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("<-")) {
				token_index = i + 1;
				break;
			}
			else {
				relation_name += token_vector.get(i) + " ";
			}
		}
		relation_name = relation_name.trim();

		// Get the expression vector
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) {
				token_index = i;
				break;
			}
			else {
				expression_vector.add(token_vector.get(i));
			}
		}

		System.out.println("Table Name: " + relation_name);
		System.out.println("Expressions List (or table name): " + expression_vector);

		Table expression_table = evaluateExpression(expression_vector);
		Engine.relations_database.put(relation_name, expression_table);
	}

	public static Table selectQuery(Vector<String> token_vector) {
		Vector<String> condition_vector = new Vector<String>();
		Vector<String> expression_vector = new Vector<String>();
		Integer token_index = 2;

		// Get the condition vector
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(")")) { 
				condition_vector.add(token_vector.get(i)); // Keep parentheses for evaluating conditions
				token_index = i + 1;
				break;
			}
			else {
				condition_vector.add(token_vector.get(i));
			}
		}

		// Get the expression vector
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) {
				expression_vector.add(token_vector.get(i)); // Keep semicolon for evaluating expressions
				token_index = i;
				break;
			}
			else {
				expression_vector.add(token_vector.get(i));
			}
		}

		System.out.println("Conditions List:" + condition_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Check if expression vector contains just a table name
		if (Engine.tableExists(expression_vector.get(0))) {
			Table selection_table = Engine.selection(expression_vector.get(0), condition_vector);
			return selection_table;
		}
		// Evaluate the expression
		else {
			Table expression_table = evaluateExpression(expression_vector);
			Table selection_table = Engine.selection(expression_table.relation_name, condition_vector);
			return selection_table;
		}
	}

	public static Table projectQuery(Vector<String> token_vector){
		Vector<String> attribute_list_vector = new Vector<String>();
		Vector<String> expression_vector = new Vector<String>();
		Integer token_index = 1;

		// Move the token_index to the beginning of the attribute list
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("(")) { 
				token_index = i + 1;
				break;
			}
		}

		// Get the attribute list vector
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(")")) { 
				token_index = i + 1;
				break;
			}
			else {
				attribute_list_vector.add(token_vector.get(i));
			}
		}

		// Get the expression vector
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) {
				expression_vector.add(token_vector.get(i)); // Keep semicolon for evaluating expressions
				token_index = i;
				break;
			}
			else {
				expression_vector.add(token_vector.get(i));
			}
		}

		System.out.println("Attributes List:" + attribute_list_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Check if expression vector contains just a table name
		if (Engine.tableExists(expression_vector.get(0))) {
			Table selection_table = Engine.projection(expression_vector.get(0), attribute_list_vector);
			return selection_table;
		}
		// Evaluate the expression
		else {
			Table expression_table = evaluateExpression(expression_vector);
			Table selection_table = Engine.projection(expression_table.relation_name, attribute_list_vector);
			return selection_table;
		}
	}

	public static Table renameQuery(Vector<String> token_vector){
		
		return null;
	}

	public static Table setUnionQuery(Vector<String> token_vector){
		
		return null;
	}

	public static Table setDifferenceQuery(Vector<String> token_vector){
		
		return null;
	}

	public static Table crossProductQuery(Vector<String> token_vector){
		
		return null;
	}

	public static Table naturalJoinQuery(Vector<String> token_vector){
		
		return null;
	}

}
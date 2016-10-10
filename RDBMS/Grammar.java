import java.util.*;
import java.io.*;

public class Grammar {
   
	private ArrayList<String> token_arrayList = new ArrayList<String>();

	Grammar(String line) {

		// Tokenize the input and store in a vector
		String delimiters = "(){};= \t\n\r\f";
		StringTokenizer st = new StringTokenizer(line, delimiters, true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!token.trim().isEmpty() && !token.equals(",")) {
				token = token.replaceAll(",", "");
				token_arrayList.add(token);
			 }
		}

		// Reconnect all double operators
		String[] operators = {"!", "<", ">", "="};
		for (int i = 1; i < token_arrayList.size(); i++) {
			for (String operator : operators) {
				if (token_arrayList.get(i-1).equals(operator) && token_arrayList.get(i).equals("=")) {
					token_arrayList.set(i-1, operator + "=");
					token_arrayList.remove(i);
				}
			}
		}

		System.out.println("\nTokenized ArrayList: " + token_arrayList);

		tokenloop:
		for (String token : token_arrayList) {
			switch(token) {
				// QUERIES
				case "<-":
					System.out.println("QUERY invoked");
					Queries.queryQuery(token_arrayList);
					break tokenloop;
				case "select":
					System.out.println("SELECT invoked");
					Queries.selectQuery(token_arrayList);
					break tokenloop;
				case "project":
					System.out.println("PROJECT invoked");
					Queries.projectQuery(token_arrayList);
					break tokenloop;
				case "rename":
					System.out.println("RENAME invoked");
					Queries.renameQuery(token_arrayList);
					break tokenloop;
				case "+":
					System.out.println("SET UNION invoked");
					Queries.setUnionQuery(token_arrayList);
					break tokenloop;
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					Queries.setDifferenceQuery(token_arrayList);
					break tokenloop;
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					Queries.crossProductQuery(token_arrayList);
					break tokenloop;
				case "JOIN":
					System.out.println("NATURAL JOIN invoked");
					Queries.naturalJoinQuery(token_arrayList);
					break tokenloop;
				// COMMANDS
				case "CREATE":
					System.out.println("CREATE TABLE invoked");
					Commands.createCommand(token_arrayList);
					break tokenloop;
				case "DROP":
					System.out.println("DROP TABLE invoked");
					Commands.dropCommand(token_arrayList);
					break tokenloop;
				case "INSERT":
					System.out.println("INSERT invoked");
					Commands.insertCommand(token_arrayList);
					break tokenloop;
				case "UPDATE":
					System.out.println("UPDATE ROW invoked");
					Commands.updateCommand(token_arrayList);
					break tokenloop;
				case "DELETE":
					System.out.println("DELETE ROW invoked");
					Commands.deleteCommand(token_arrayList);
					break tokenloop;
				case "SHOW":
					System.out.println("SHOW invoked");
					Commands.showCommand(token_arrayList);
					break tokenloop;
				case "OPEN":
					System.out.println("OPEN invoked");
					Commands.openCommand(token_arrayList);
					break tokenloop;
				case "WRITE":
					System.out.println("OPEN invoked");
					Commands.writeCommand(token_arrayList);
					break tokenloop;
				case "CLOSE":
					System.out.println("CLOSE invoked");
					Commands.closeCommand(token_arrayList);
					break tokenloop;
				case "EXIT":
					System.out.println("EXIT invoked");
					Commands.exitCommand();
					break tokenloop;
			}
		}
	}

	public static Integer skipTokens(ArrayList<String> token_arrayList, String token) {
		for (int i = 0; i < token_arrayList.size(); i++) {
			if (!token_arrayList.get(i).equalsIgnoreCase(token)) {
				return i;
			}
		}
		return 0;
	}

	public static Integer skipToToken(ArrayList<String> token_arrayList, Integer token_index, String token) {
		for (int i = token_index; i < token_arrayList.size(); i++) {
			if (token_arrayList.get(i).equalsIgnoreCase(token)) {
				return i;
			}
		}
		return 0;
	}

	public static ArrayList<String> retrieveTokens(ArrayList<String> token_arrayList, Integer token_index, String token, Boolean value) {
		ArrayList<String> temp_vector = new ArrayList<String>();

		for (int i = token_index; i < token_arrayList.size(); i++) {
			if (token_arrayList.get(i).equals(token)) {
				token_index = i + 1;
				if (value) {
					temp_vector.add(token_arrayList.get(i));			
				}
				break;
			}
			else {
				temp_vector.add(token_arrayList.get(i));
			}
		}
		return temp_vector;
	}

	public static Table evaluateExpression(ArrayList<String> token_arrayList) {
		// Start off by evaluating the expression
		for (int i = 0; i < token_arrayList.size(); i++) {
			String token = token_arrayList.get(i);
			switch(token.toLowerCase()) {
				case "select":
					System.out.println("SELECT invoked");
					return Queries.selectQuery(token_arrayList);
				case "project":
					System.out.println("PROJECT invoked");
					return Queries.projectQuery(token_arrayList);
				case "rename":
					System.out.println("RENAME invoked");
					return Queries.renameQuery(token_arrayList);
				case "+":
					System.out.println("SET UNION invoked");
					return Queries.setUnionQuery(token_arrayList);
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					return Queries.setDifferenceQuery(token_arrayList);
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					return Queries.crossProductQuery(token_arrayList);
				case "join":
					System.out.println("NATURAL JOIN invoked");
					return Queries.naturalJoinQuery(token_arrayList);
			}
		}

		// If no expression is found, check if it is just a relation name
		if (isRelationName(token_arrayList)) {
			Table retrieved_table = Engine.relations_database.get(token_arrayList.get(0));
			return retrieved_table;
		}
		// No relation name or expression found
		else {
			return null;
		}
	}
 
	public static Boolean isRelationName(ArrayList<String> token_arrayList) {
		Boolean value = true;
		Integer token_index = 0;

		// Remove leading parentheses
		for (int i = token_index; i < token_arrayList.size(); i++) {
			if (token_arrayList.get(i).equals("(")) {
				token_index = i + 1;
			}
			else {
				break;
			}
		}

		// Check if next token is a relation name
		String[] algebraic_expressions = {"select", "project", "rename", "+", "-", "*", "JOIN"};
		for (int i = token_index; i < token_arrayList.size(); i++) {
			for (String expression : algebraic_expressions) {
				if (token_arrayList.get(i).equals(expression)) {
					value = false; // Algebraic expression found
				}
			}
		}
		return value;
	}

	public static String getRelationName(ArrayList<String> token_arrayList) {
		String relation_name = "";

		if (token_arrayList.contains("CREATE") ||
			token_arrayList.contains("SELECT") ||
			token_arrayList.contains("PROJECT") ||
			token_arrayList.contains("+") ||
			token_arrayList.contains("-") ||
			token_arrayList.contains("*") ||
			token_arrayList.contains("JOIN")) {
				System.out.println("Nested table detected in getRelationName");
				System.out.println("(This shouldn't happen!)");
		}
		else {
			for (String token : token_arrayList) {
				if (token.equals("OPEN") ||
					token.equals("CLOSE") ||
					token.equals("WRITE") ||
					token.equals("SHOW") ||
					token.equals("DROP") ||
					token.equals("TABLE")) {
					continue;
				}
				else if (token.equals(";")) {
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
}
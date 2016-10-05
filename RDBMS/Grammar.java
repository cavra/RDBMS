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
			for (String operator : operators) {
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
					Queries.queryQuery(token_vector);
					break tokenloop;
				case "select":
					System.out.println("SELECT invoked");
					Queries.selectQuery(token_vector);
					break tokenloop;
				case "project":
					System.out.println("PROJECT invoked");
					Queries.projectQuery(token_vector);
					break tokenloop;
				case "rename":
					System.out.println("RENAME invoked");
					Queries.renameQuery(token_vector);
					break tokenloop;
				case "+":
					System.out.println("SET UNION invoked");
					Queries.setUnionQuery(token_vector);
					break tokenloop;
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					Queries.setDifferenceQuery(token_vector);
					break tokenloop;
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					Queries.crossProductQuery(token_vector);
					break tokenloop;
				case "JOIN":
					System.out.println("NATURAL JOIN invoked");
					Queries.naturalJoinQuery(token_vector);
					break tokenloop;
				// COMMANDS
				case "CREATE":
					System.out.println("CREATE TABLE invoked");
					Commands.createCommand(token_vector);
					break tokenloop;
				case "DROP":
					System.out.println("DROP TABLE invoked");
					Commands.dropCommand(token_vector);
					break tokenloop;
				case "INSERT":
					System.out.println("INSERT invoked");
					Commands.insertCommand(token_vector);
					break tokenloop;
				case "UPDATE":
					System.out.println("UPDATE ROW invoked");
					Commands.updateCommand(token_vector);
					break tokenloop;
				case "DELETE":
					System.out.println("DELETE ROW invoked");
					Commands.deleteCommand(token_vector);
					break tokenloop;
				case "SHOW":
					System.out.println("SHOW invoked");
					Commands.showCommand(token_vector);
					break tokenloop;
				case "OPEN":
					System.out.println("OPEN invoked");
					Commands.openCommand(token_vector);
					break tokenloop;
				case "WRITE":
					System.out.println("OPEN invoked");
					Commands.writeCommand(token_vector);
					break tokenloop;
				case "CLOSE":
					System.out.println("CLOSE invoked");
					Commands.closeCommand(token_vector);
					break tokenloop;
				case "EXIT":
					System.out.println("EXIT invoked");
					Commands.exitCommand();
					break tokenloop;
			}
		}
	}

	public static Boolean isRelationName(Vector<String> token_vector) {
		Boolean value = true;
	
		String[] algebraic_expressions = {"select", "project", "rename", "+", "-", "*", "JOIN"};
		for (int i = 0; i < token_vector.size(); i++) {
			for (String expression : algebraic_expressions) {
				if (token_vector.get(i).equals(expression)) {
					value = false; // Algebraic expression found
				}
			}
		}
		return value;
	}

	public static Table evaluateExpression(Vector<String> token_vector) { //eventually return table
		for (int i = 0; i < token_vector.size(); i++) {
			String token = token_vector.get(i);
			switch(token) {
				case "select":
					System.out.println("SELECT invoked");
					return Queries.selectQuery(token_vector);
				case "project":
					System.out.println("PROJECT invoked");
					return Queries.projectQuery(token_vector);
				case "rename":
					System.out.println("RENAME invoked");
					return Queries.renameQuery(token_vector);
				case "+":
					System.out.println("SET UNION invoked");
					return Queries.setUnionQuery(token_vector);
				case "-":
					System.out.println("SET DIFFERENCE invoked");
					return Queries.setDifferenceQuery(token_vector);
				case "*":
					System.out.println("CROSS PRODUCT invoked");
					return Queries.crossProductQuery(token_vector);
				case "JOIN":
					System.out.println("NATURAL JOIN invoked");
					return Queries.naturalJoinQuery(token_vector);
				 // case "(":
				 // 	System.out.println("NESTED EXPRESSION detected");
				 // 	Vector<String> nested_expression_vector = new Vector<String>();

					// // Store the nested expression
					// for (int j = i + 1; j < token_vector.size(); j++) {
					// 	nested_expression_vector.add(token_vector.get(j));
					// }
					// return evaluateExpression(nested_expression_vector);
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
}
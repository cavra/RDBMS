import java.util.*;
import java.io.*;

public class Queries {

	public static void queryQuery(ArrayList<String> token_ArrayList) {

		// Skip leading parentheses
		Integer token_index = Grammar.skipTokens(token_ArrayList, "(");

		// Get the new relation name
		String relation_name = "";
		ArrayList<String> relation_name_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, "<-", false);
		for (String string : relation_name_ArrayList) {
			relation_name += string + " ";
		}
		relation_name = relation_name.trim();
		token_index += relation_name_ArrayList.size() + 1;

		// Get the expression ArrayList
		ArrayList<String> expression_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("Table Name: " + relation_name);
		System.out.println("Expressions List (or table name): " + expression_ArrayList);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_ArrayList);
		Engine.relations_database.put(relation_name, expression_table);
		System.out.println("Relation " + relation_name + " received data from Relation " + expression_table.relation_name);
	}

	public static Table selectQuery(ArrayList<String> token_ArrayList) {

		// Skip to the condition
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "select");
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "(") + 1;

		// Get the condition ArrayList
		ArrayList<String> condition_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ")", false);
		token_index += condition_ArrayList.size() + 1;

		// Get the expression ArrayList
		ArrayList<String> expression_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("Conditions List:" + condition_ArrayList);
		System.out.println("Expressions List (or table name): " + expression_ArrayList);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_ArrayList);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table selection_table = Engine.selection("Temp Expression Table", condition_ArrayList);
		return selection_table;
	}

	public static Table projectQuery(ArrayList<String> token_ArrayList){

		// Skip to the condition
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "project");
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "(") + 1;

		// Get the attribute list ArrayList
		ArrayList<String> attribute_list_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ")", false);
		token_index += attribute_list_ArrayList.size() + 1;

		// Get the expression ArrayList
		ArrayList<String> expression_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("Attributes List:" + attribute_list_ArrayList);
		System.out.println("Expressions List (or table name): " + expression_ArrayList);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_ArrayList);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table projection_table = Engine.projection("Temp Expression Table", attribute_list_ArrayList);
		return projection_table;
	}

	public static Table renameQuery(ArrayList<String> token_ArrayList) {

		// Skip to the attribute list
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "rename") + 1;
		token_index = Grammar.skipToToken(token_ArrayList, token_index, "(") + 1;

		// Get the attribute list ArrayList
		ArrayList<String> attribute_list_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ")", false);
		token_index += attribute_list_ArrayList.size() + 1;

		// Get the expression ArrayList
		ArrayList<String> expression_ArrayList = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("Attributes List:" + attribute_list_ArrayList);
		System.out.println("Expressions List (or table name): " + expression_ArrayList);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_ArrayList);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table rename_table = Engine.rename("Temp Expression Table", attribute_list_ArrayList);
		return rename_table;
	}

	public static Table setUnionQuery(ArrayList<String> token_ArrayList) {

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_ArrayList, "(");

		// Store the first expression
		ArrayList<String> expression_ArrayList1 = Grammar.retrieveTokens(token_ArrayList, token_index, "+", false);
		token_index += expression_ArrayList1.size() + 1;

		// Store the second expression
		ArrayList<String> expression_ArrayList2 = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("First Expression:" + expression_ArrayList1);
		System.out.println("Second Expression:" + expression_ArrayList2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_ArrayList1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_ArrayList2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table set_union_table = Engine.setUnion("Temp Set Union Table", relation_name1, relation_name2);
		return set_union_table;
	}

	public static Table setDifferenceQuery(ArrayList<String> token_ArrayList){

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_ArrayList, "(");

		// Store the first expression
		ArrayList<String> expression_ArrayList1 = Grammar.retrieveTokens(token_ArrayList, token_index, "-", false);
		token_index += expression_ArrayList1.size() + 1;

		// Store the second expression
		ArrayList<String> expression_ArrayList2 = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("First Expression:" + expression_ArrayList1);
		System.out.println("Second Expression:" + expression_ArrayList2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_ArrayList1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_ArrayList2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table set_difference_table = Engine.setDifference("Temp Set Difference Table", relation_name1, relation_name2);
		return set_difference_table;	
	}

	public static Table crossProductQuery(ArrayList<String> token_ArrayList){

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_ArrayList, "(");

		// Store the first expression
		ArrayList<String> expression_ArrayList1 = Grammar.retrieveTokens(token_ArrayList, token_index, "*", false);
		token_index += expression_ArrayList1.size() + 1;

		// Store the second expression
		ArrayList<String> expression_ArrayList2 = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("First Expression:" + expression_ArrayList1);
		System.out.println("Second Expression:" + expression_ArrayList2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_ArrayList1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_ArrayList2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table cross_product_table = Engine.crossProduct("Temp Cross Product Table", relation_name1, relation_name2);
		return cross_product_table;
	}

	public static Table naturalJoinQuery(ArrayList<String> token_ArrayList){
		
		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_ArrayList, "(");

		// Store the first expression
		ArrayList<String> expression_ArrayList1 = Grammar.retrieveTokens(token_ArrayList, token_index, "JOIN", false);
		token_index += expression_ArrayList1.size() + 1;

		// Store the second expression
		ArrayList<String> expression_ArrayList2 = Grammar.retrieveTokens(token_ArrayList, token_index, ";", true);

		System.out.println("First Expression:" + expression_ArrayList1);
		System.out.println("Second Expression:" + expression_ArrayList2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_ArrayList1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_ArrayList2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table natural_join_table = Engine.naturalJoin("Temp Natural Join Table", relation_name1, relation_name2);
		return natural_join_table;
	}
}
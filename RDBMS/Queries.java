import java.util.*;
import java.io.*;

public class Queries {

	public static void queryQuery(Vector<String> token_vector) {

		// Skip leading parentheses
		Integer token_index = Grammar.skipTokens(token_vector, "(");

		// Get the new relation name
		String relation_name = "";
		Vector<String> relation_name_vector = Grammar.retrieveTokens(token_vector, token_index, "<-", false);
		for (String string : relation_name_vector) {
			relation_name += string + " ";
		}
		relation_name = relation_name.trim();
		token_index += relation_name_vector.size() + 1;

		// Get the expression vector
		Vector<String> expression_vector = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("Table Name: " + relation_name);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_vector);
		Engine.relations_database.put(relation_name, expression_table);
	}

	public static Table selectQuery(Vector<String> token_vector) {

		// Skip to the condition
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_vector, token_index, "select");
		token_index = Grammar.skipToToken(token_vector, token_index, "(") + 1;

		// Get the condition vector
		Vector<String> condition_vector = Grammar.retrieveTokens(token_vector, token_index, ")", false);
		token_index += condition_vector.size() + 1;

		// Get the expression vector
		Vector<String> expression_vector = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("Conditions List:" + condition_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_vector);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table selection_table = Engine.selection("Temp Expression Table", condition_vector);
		return selection_table;
	}

	public static Table projectQuery(Vector<String> token_vector){

		// Skip to the condition
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_vector, token_index, "project");
		token_index = Grammar.skipToToken(token_vector, token_index, "(") + 1;

		// Get the attribute list vector
		Vector<String> attribute_list_vector = Grammar.retrieveTokens(token_vector, token_index, ")", false);
		token_index += attribute_list_vector.size() + 1;

		// Get the expression vector
		Vector<String> expression_vector = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("Attributes List:" + attribute_list_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_vector);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table selection_table = Engine.projection("Temp Expression Table", attribute_list_vector);
		return selection_table;
	}

	public static Table renameQuery(Vector<String> token_vector) {

		// Skip to the attribute list
		Integer token_index = 0;
		token_index = Grammar.skipToToken(token_vector, token_index, "rename") + 1;
		token_index = Grammar.skipToToken(token_vector, token_index, "(") + 1;

		// Get the attribute list vector
		Vector<String> attribute_list_vector = Grammar.retrieveTokens(token_vector, token_index, ")", false);
		token_index += attribute_list_vector.size() + 1;

		// Get the expression vector
		Vector<String> expression_vector = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("Attributes List:" + attribute_list_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Evaluate and create the table
		Table expression_table = Grammar.evaluateExpression(expression_vector);
		Engine.relations_database.put("Temp Expression Table", expression_table);
		Table rename_table = Engine.rename("Temp Expression Table", attribute_list_vector);
		return rename_table;
	}

	public static Table setUnionQuery(Vector<String> token_vector) {

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_vector, "(");

		// Store the first expression
		Vector<String> expression_vector1 = Grammar.retrieveTokens(token_vector, token_index, "+", false);
		token_index += expression_vector1.size() + 1;

		// Store the second expression
		Vector<String> expression_vector2 = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("First Expression:" + expression_vector1);
		System.out.println("Second Expression:" + expression_vector2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table set_union_table = Engine.setUnion("Temp Set Union Table", relation_name1, relation_name2);
		return set_union_table;
	}

	public static Table setDifferenceQuery(Vector<String> token_vector){

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_vector, "(");

		// Store the first expression
		Vector<String> expression_vector1 = Grammar.retrieveTokens(token_vector, token_index, "-", false);
		token_index += expression_vector1.size() + 1;

		// Store the second expression
		Vector<String> expression_vector2 = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("First Expression:" + expression_vector1);
		System.out.println("Second Expression:" + expression_vector2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table set_difference_table = Engine.setDifference("Temp Set Difference Table", relation_name1, relation_name2);
		return set_difference_table;	
	}

	public static Table crossProductQuery(Vector<String> token_vector){

		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_vector, "(");

		// Store the first expression
		Vector<String> expression_vector1 = Grammar.retrieveTokens(token_vector, token_index, "*", false);
		token_index += expression_vector1.size() + 1;

		// Store the second expression
		Vector<String> expression_vector2 = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("First Expression:" + expression_vector1);
		System.out.println("Second Expression:" + expression_vector2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table cross_product_table = Engine.crossProduct("Temp Cross Product Table", relation_name1, relation_name2);
		return cross_product_table;
	}

	public static Table naturalJoinQuery(Vector<String> token_vector){
		
		// Remove leading parentheses
		Integer token_index = Grammar.skipTokens(token_vector, "(");

		// Store the first expression
		Vector<String> expression_vector1 = Grammar.retrieveTokens(token_vector, token_index, "JOIN", false);
		token_index += expression_vector1.size() + 1;

		// Store the second expression
		Vector<String> expression_vector2 = Grammar.retrieveTokens(token_vector, token_index, ";", true);

		System.out.println("First Expression:" + expression_vector1);
		System.out.println("Second Expression:" + expression_vector2);

		// Evaluate first expression
		Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
		Engine.relations_database.put("Temp Expression Table1", expression_table1);
		String relation_name1 = "Temp Expression Table1";

		// Evaluate second expression
		Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
		Engine.relations_database.put("Temp Expression Table2", expression_table2);
		String relation_name2 = "Temp Expression Table2";

		// Create the table
		Table natural_join_table = Engine.naturalJoin("Temp Natural Join Table", relation_name1, relation_name2);
		return natural_join_table;
	}
}
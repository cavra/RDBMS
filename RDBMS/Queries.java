import java.util.*;
import java.io.*;

public class Queries {

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

		Table expression_table = Grammar.evaluateExpression(expression_vector);
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

		// Check if expression vector contains just a table name and it exists
		if (Grammar.isRelationName(expression_vector)) {
			Table selection_table = Engine.selection(expression_vector.get(0), condition_vector);
			return selection_table;
		}
		// Evaluate the expression
		else {
			Table expression_table = Grammar.evaluateExpression(expression_vector);
			Engine.relations_database.put("Temp Expression Table", expression_table);
			Table selection_table = Engine.selection("Temp Expression Table", condition_vector);
			return selection_table;
		}
	}

	public static Table projectQuery(Vector<String> token_vector){
		Vector<String> attribute_list_vector = new Vector<String>();
		Vector<String> expression_vector = new Vector<String>();
		Integer token_index = 1;

		System.out.println(token_vector);
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
			if (token_vector.get(i).equals(";") || 
				token_vector.get(i).equals(")")) {
				expression_vector.add(token_vector.get(i)); // Keep semicolon for evaluating expressions
				token_index = i;
				break;
			}
			else {
				expression_vector.add(token_vector.get(i));
				System.out.println(token_vector.get(i));
			}
		}

		System.out.println("Attributes List:" + attribute_list_vector);
		System.out.println("Expressions List (or table name): " + expression_vector);

		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector)) {
			Table selection_table = Engine.projection(expression_vector.get(0), attribute_list_vector);
			return selection_table;
		}
		// Evaluate the expression
		else {
			Table expression_table = Grammar.evaluateExpression(expression_vector);
			Engine.relations_database.put("Temp Expression Table", expression_table);
			Table selection_table = Engine.projection("Temp Expression Table", attribute_list_vector);
			return selection_table;
		}
	}

	// This function is the same as project, except it calls rename at the end
	// Which isn't defined in the engine yet
	public static Table renameQuery(Vector<String> token_vector){
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
		if (Grammar.isRelationName(expression_vector)) {
			//Table selection_table = Engine.projection(expression_vector.get(0), attribute_list_vector);
			//return selection_table;
		}
		// Evaluate the expression
		else {
			Table expression_table = Grammar.evaluateExpression(expression_vector);
			Engine.relations_database.put("Temp Expression Table", expression_table);
			//Table selection_table = Engine.projection("Temp Expression Table", attribute_list_vector);
			//return selection_table;
		}
		return null;
	}

	public static Table setUnionQuery(Vector<String> token_vector) {
		Vector<String> expression_vector1 = new Vector<String>();
		Vector<String> expression_vector2 = new Vector<String>();
		String relation_name1 = "";
		String relation_name2 = "";
		Integer token_index = 0;

		// Store the first expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("+")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector1.add(token_vector.get(i));
			}
		}

		// Store the second expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector2.add(token_vector.get(i));
			}
		}

		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector1)) {
			relation_name1 = expression_vector1.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
			Engine.relations_database.put("Temp Expression Table1", expression_table1);
			relation_name1 = "Temp Expression Table1";
		}
		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector2)) {
			relation_name2 = expression_vector2.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
			Engine.relations_database.put("Temp Expression Table2", expression_table2);
			relation_name2 = "Temp Expression Table2";
		}

		Table set_union_table = Engine.setUnion("Temp Set Union Table", relation_name1, relation_name2);
		return set_union_table;
	}

	public static Table setDifferenceQuery(Vector<String> token_vector){
		Vector<String> expression_vector1 = new Vector<String>();
		Vector<String> expression_vector2 = new Vector<String>();
		String relation_name1 = "";
		String relation_name2 = "";
		Integer token_index = 0;

		// Store the first expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("-")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector1.add(token_vector.get(i));
			}
		}

		// Store the second expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector2.add(token_vector.get(i));
			}
		}

		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector1)) {
			relation_name1 = expression_vector1.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
			Engine.relations_database.put("Temp Expression Table1", expression_table1);
			relation_name1 = "Temp Expression Table1";
		}
		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector2)) {
			relation_name2 = expression_vector2.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
			Engine.relations_database.put("Temp Expression Table2", expression_table2);
			relation_name2 = "Temp Expression Table2";
		}

		Table set_difference_table = Engine.setDifference("Temp Set Difference Table", relation_name1, relation_name2);
		return set_difference_table;	
	}

	public static Table crossProductQuery(Vector<String> token_vector){
		Vector<String> expression_vector1 = new Vector<String>();
		Vector<String> expression_vector2 = new Vector<String>();
		String relation_name1 = "";
		String relation_name2 = "";
		Integer token_index = 0;

		// Store the first expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("*")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector1.add(token_vector.get(i));
			}
		}

		// Store the second expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector2.add(token_vector.get(i));
			}
		}

		System.out.println(expression_vector1);

		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector1)) {
			relation_name1 = expression_vector1.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
			Engine.relations_database.put("Temp Expression Table1", expression_table1);
			relation_name1 = "Temp Expression Table1";
		}
		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector2)) {
			relation_name2 = expression_vector2.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
			Engine.relations_database.put("Temp Expression Table2", expression_table2);
			relation_name2 = "Temp Expression Table2";
		}

		Table cross_product_table = Engine.crossProduct("Temp Cross Product Table", relation_name1, relation_name2);
		return cross_product_table;
	}

	public static Table naturalJoinQuery(Vector<String> token_vector){
		Vector<String> expression_vector1 = new Vector<String>();
		Vector<String> expression_vector2 = new Vector<String>();
		String relation_name1 = "";
		String relation_name2 = "";
		Integer token_index = 0;

		// Store the first expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals("JOIN")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector1.add(token_vector.get(i));
			}
		}

		// Store the second expression
		for (int i = token_index; i < token_vector.size(); i++) {
			if (token_vector.get(i).equals(";")) { 
				token_index = i + 1;
				break;
			}
			else {
				expression_vector2.add(token_vector.get(i));
			}
		}

		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector1)) {
			relation_name1 = expression_vector1.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table1 = Grammar.evaluateExpression(expression_vector1);
			Engine.relations_database.put("Temp Expression Table1", expression_table1);
			relation_name1 = "Temp Expression Table1";
		}
		// Check if expression vector contains just a table name
		if (Grammar.isRelationName(expression_vector2)) {
			relation_name2 = expression_vector2.get(0);
		}
		// Evaluate the expression
		else {
			Table expression_table2 = Grammar.evaluateExpression(expression_vector2);
			Engine.relations_database.put("Temp Expression Table2", expression_table2);
			relation_name2 = "Temp Expression Table2";
		}

		Table natural_join_table = Engine.naturalJoin("Temp Natural Join Table", relation_name1, relation_name2);
		return natural_join_table;
	}

}
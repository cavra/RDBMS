import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class Grammar {
   	
   	// Basics
   	String alpha_p = "[a-zA-Z]+";
	String digit_p = "[0-9]+";
  	String identifier_p = "[a-zA-Z][a-zA-Z0-9]+";
  	String relation_name_p = identifier_p;
	String attribute_name_p = identifier_p;

	String integer_p = digit_p + "[0-9]"; // More than 1
	String literal_p = integer_p + "| " + "\'[^\']*\'"; // This probably doesn't work; needs testing

	String operand_p = attribute_name_p + "| " + literal_p;
	String comparison_p = operand_p + "==|!=|<|>|<=|>=" + operand_p;
	String conjunction_p = comparison_p + "[{]" + "&&" + comparison_p + "[}]" + "| " + 
		"(" + comparison_p + "[{]" + "&&" + comparison_p + "[}]" + "[{]" + "||" + comparison_p + "[{]" + "&&" + comparison_p + "[}]" + "[}]" + ")";
	String condition_p = conjunction_p + "[{]" + "||" + conjunction_p + "[}]";

	String attribute_list_p = attribute_name_p + "[{]" + attribute_name_p + "[}]";

	String selection_p = "SELECT";// + "[(]" + condition_p + ")"; // + atomic_expression_p;
  	String projection_p = "PROJECT";// + "[(]" + attribute_list_p + ")"; // + atomic_expression_p;
  	String renaming_p = "RENAME";// + "[(]" + attribute_list_p + ")"; // + atomic_expression_p;
  	String union_p = "^[a-zA-Z]+" + "+" + "^[a-zA-Z]+"; // this should try to catch atomic_expression
  	String difference_p = "^[a-zA-Z]+" + "-" + "^[a-zA-Z]+";
  	String product_p = "^[a-zA-Z]+" + "[*]" + "^[a-zA-Z]+";
  	String natural_join_p = "^[a-zA-Z]+" + "JOIN" + "^[a-zA-Z]+";

	// missing atomic_expression
   	String expression_p =	selection_p + "| " +
   							projection_p + "| " +
   							renaming_p + "| " +
   							union_p + "| " +
   							difference_p + "| " +
   							product_p + "| " +
   							natural_join_p;

   	String atomic_expression_p = relation_name_p + "| " + expression_p;

   	String query_p = relation_name_p + "<-" + expression_p + ";";
	
	String type_p = "VARCHAR" + "(" + integer_p + ")" + "| " + "INTEGER";
	String typed_attribute_list_p = attribute_name_p + type_p + "[{]" + attribute_name_p + type_p + "[}]";
	
	// Commands
	String open_cmd_p = "OPEN" + relation_name_p + ";";
	String close_cmd_p = "CLOSE" + relation_name_p + ";";
	String write_cmd_p = "WRITE" + relation_name_p + ";";
	String exit_cmd_p = "EXIT;";
	String show_cmd_p = "SHOW" + atomic_expression_p + ";";

	String create_cmd_p = "CREATETABLE" + relation_name_p + "(" + typed_attribute_list_p + ")" + "PRIMARYKEY" + "(" + attribute_list_p + ")" + ";";
	String drop_cmd_p = "DROPTABLE" + relation_name_p + ";";

	String insert_cmd_p1 = "INSERTINTO" + relation_name_p + "VALUESFROM" + "[(]" + literal_p + "[{]" + literal_p + "[}]" + ")" + ";";
	String insert_cmd_p2 = "INSERTINTO" + relation_name_p + "VALUESFROMRELATION" + expression_p + ";";

	String update_cmd_p = "UPDATE" + relation_name_p + "SET" + attribute_name_p 
		+ "=" + literal_p + "[{]" + attribute_name_p + "=" + literal_p + "[}]" + "WHERE" + condition_p + ";";
	String delete_cmd_p = "DELETEFROM" + relation_name_p + "WHERE" + condition_p + ";";

	String program_p = "[{]" + query_p + "| " + "command" + "[}]"; // "command" here can be any of the commands above

   	String[] regex_patterns = {

		selection_p,
	  	projection_p,
	  	renaming_p,
	  	union_p,
	  	difference_p,
	  	product_p,
	  	natural_join_p,

	   	query_p,
		
		type_p,
		typed_attribute_list_p,
		
		open_cmd_p,
		close_cmd_p,
		write_cmd_p,
		exit_cmd_p,
		show_cmd_p,

		create_cmd_p,
		drop_cmd_p,

		insert_cmd_p1,
		insert_cmd_p2,

		update_cmd_p,
		delete_cmd_p,
	};

	Grammar(String line){

		line = line.replaceAll(" ", "");

		System.out.println("\nLine input is: " + line);
	
		for (int i = 0; i < regex_patterns.length; i++) {

			// Create the regex pattern
	      	Pattern r = Pattern.compile(regex_patterns[i]);

			// Now create matcher object.
			if (line.matches(regex_patterns[i])) {
			//Matcher m = r.matcher(line);
			//if (m.find( )) {
				System.out.println("Current index: " + i);
				System.out.println("Found pattern: " + r + "\n");
			}else {
				//System.out.println("NO MATCH" + "\n");
			}
		}
	}

}


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import java.io.*;

public class Grammar {
   	
   	// this might be deprecated

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

	String insert_cmd_p1 = "INSERTINTO" + relation_name_p + "VALUESFROM" + "[(]" + literal_p + "[{]" + literal_p + "[}]" + "[)]" + ";";
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

		// Remove all whitespace to handle various inputs
		String command = line.replaceAll(" ", "");

		//command = command.toUpperCase(); // Do we need to do this? 

		// Get the most nested command
		String nested_command = getMostNested(command);

		// Check all the possible commands
		// in each if statement here, get the first part of the string and second part and 
		// call the proper engine function, get the proper table or whatever, 
		// and then return it here if necessary so the parser can handle nested stuff
		if (command.contains("CREATETABLE")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("DROPTABLE")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("OPEN")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("CLOSE")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("WRITE")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("EXIT")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("SHOW")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("UPDATE")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("DELETEFROM")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("INSERTINTO")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("SELECT")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("PROJECT")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("RENAME")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("+")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("-")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("*")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("JOIN")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("<-")) {
			System.out.println("\nFound command: " + command);
		}
		else if (command.contains("==") || 
			command.contains("!=") || 
			command.contains("<") || 
			command.contains(">") || 
			command.contains("<=") ||
			command.contains(">=")) {
			System.out.println("\nFound command: " + command);
			}
		else {
			System.out.println("\nNOT found command: " + command);
		}

		// After we've done this, we need to see if there are any more commands left besides
		// the most nested one we've just done

		// This is the old Regex way of doing things, leave it here JIC
		/*System.out.println("\nLine input is: " + command);
	
		for (int i = 0; i < regex_patterns.length; i++) {

			// Create the regex pattern
	      	Pattern r = Pattern.compile(regex_patterns[i]);

			// Now create matcher object.
			if (command.matches(regex_patterns[i])) {
			//Matcher m = r.matcher(command);
			//if (m.find( )) {
				System.out.println("Current index: " + i);
				System.out.println("Found pattern: " + r + "\n");
			}else {
				//System.out.println("NO MATCH" + "\n");
			}
		}*/
	}

	public String getMostNested(String command) {
		if (command.contains("(")) {
			String new_command = command.substring(command.indexOf("("), command.indexOf(")"));
			getMostNested(new_command);
		}
		else if (command.charAt(0) == "(") {
			String newcommand = command.substring(1, command.size() - 2); // Not sure what this is for, Reed explain?
		}
		else {
			// Found it!
			return command;
		}

	}

}


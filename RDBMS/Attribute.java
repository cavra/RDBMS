import java.util.*;

public class Attribute {
	
	public String name;
	public String type;
	public Integer varchar_length = 0;

	Attribute(String attribute_name, String attribute_type) {

		name = attribute_name;

		if (attribute_type.equals("INTEGER")) {
			type = attribute_type;
		}
		else if (attribute_type.contains("VARCHAR")) {
			String[] attribute_type_array = attribute_type.split("\\s");
			if (attribute_type_array.length > 1) {
				type = attribute_type_array[0];
				varchar_length = Integer.parseInt(attribute_type_array[1]);
			}
			else {
				System.out.println("Missing VARCHAR length for attribtue!");
			}
		}
		else {
			System.out.println("Invalid attribute_type detected: " + attribute_type);
		}
	}

	public boolean isValid(String attribute){
		String integer_regex = "[0-9]+";
		String varchar_regex = "[a-zA-Z][a-zA-Z0-9_]+";

		if (attribute.matches(integer_regex) && 
			type == "INTEGER")
			return true;
		else if (attribute.matches(varchar_regex) && 
			type == "VARCHAR" &&
			attribute.toCharArray().length <= varchar_length) {
			return true;
		}
		else if (attribute.matches(varchar_regex) && 
			type == "VARCHAR" &&
			attribute.toCharArray().length >= varchar_length) {
			System.out.println("Value exceeds VARCHAR size limit.");
			return false;
		}
		else {
			System.out.println("Attribute does not match either attribute_type.");
			return false;
		}
	}
}
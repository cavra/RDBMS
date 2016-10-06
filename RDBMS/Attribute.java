import java.util.*;

public class Attribute {
	
	String atttribute_name;
	String attribute_type;
	int varchar_length;

	Attribute(Vector<String> attr_vector){
		// Retrieve the attribute name
		for (int i = 0; i < attr_vector.size(); i++) {
			if (attr_vector.get(i).equals("VARCHAR")) {
				attribute_type = attr_vector.get(i);
				varchar_length = Integer.parseInt(attr_vector.get(i + 2));
				break;
			}
			else if (attr_vector.get(i).equals("INTEGER")){
				attribute_type = attr_vector.get(i);
				break;
			}
			else {
				atttribute_name += attr_vector.get(i) + " ";
			}
		}
		atttribute_name = atttribute_name.trim();
	}

	public boolean isValid(String attr){
		String integer_regex = "[0-9]+";
		String varchar_regex = "[a-zA-Z][a-zA-Z0-9_]+";

		if (attr.matches(integer_regex) && attribute_type == "INTEGER")
			return true;
		else if (attr.matches(varchar_regex) && attribute_type == "VARCHAR"){
			if (attr.toCharArray().length > varchar_length){
				System.out.println("Value exceeds varchar size limit.");
				return false;
			}
			return true;
		}
		else
			return false;
	}
}
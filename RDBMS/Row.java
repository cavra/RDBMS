import java.util.*;

public class Row {
	
	public ArrayList<String> values = new ArrayList<String>();
	public String key;

// =============================================================================
// The Row constructor
// Parameters:
// =============================================================================

	Row(ArrayList<String> values, String key) {
		this.values = values;
		this.key = key;
	}

	public Integer size() {
		return values.size();
	}
}
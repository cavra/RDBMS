
public class Engine {

	static Hashmap<String, Hashmap<String, String>> dbms_hashmap = new Hashmap<String, Hashmap<String, String>>();

	static ArrayList<String, String[]> player_list = new ArrayList<String, String[]>();
	static ArrayList<String, String[]> team_list = new ArrayList<String, String[]>();
	static ArrayList<String, String[]> sport_list = new ArrayList<String, String[]>();

	public static void main(String[] args){


	}

	public static void create_table(String entity, String[] args, String[] pk){

		String value = dbms_hashmap.get(entity);
		if (value != null) {
			break;
		}


		Hashmap<String, String[]> hmap = new HashMap<String, String[]>();
		for (i = 0; i < args.length - 1; i++) {
			hmap.put(args[i], "")
		}
		dbms_hashmap.put(entity, hmap)

	}

	void insert(String s, String[] a){

		// get the hashmap from the dbms_hashmap using string s as a key

		for (i = 0; i < args.length - 1; i++) {
			hmap.put(args[i], "")
		}		


	}

	void show(){

	}
}

import java.util.*;

public class TestList {

	public void callAll() {
		createTable();
		insertRow();
		show();
	}

	public void createTable() {

		String[] keys = {"name", "age", "jersey number"};
		String[] p_keys = {"age", "jersey number"};

		Engine.createTable("Test Table", keys, p_keys);
	}

	public void insertRow() {

		String[] values1 = {"John", "21", "07"};
		Engine.insertRow("Test Table", values1);

		String[] values2 =  {"Jacob", "20", "42"};
		Engine.insertRow("Test Table", values2);

		String[] values3 = {"Jingle", "24", "13"};
		Engine.insertRow("Test Table", values3);
	}

	public void show() {
		Engine.show("Test Table");
	}

 }
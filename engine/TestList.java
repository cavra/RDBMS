import java.util.*;

public class TestList {

	public void callAll() {

		// All functions should be called here
		createTable();
		insertRow();
		show();

		deleteRow();
		updateRow();
		show();

		dropTable();
		show();
	}

	public void createTable() {

		String[] keys = {"name", "age", "jersey number"};
		String[] p_keys = {"age", "jersey number"};

		Engine.createTable("Test Table", keys, p_keys);
	}

	public void dropTable() {
		Engine.dropTable("Test Table");
	}

	public void insertRow() {

		String[] values1 = {"John", "21", "07"};
		Engine.insertRow("Test Table", values1);

		String[] values2 =  {"Jacob", "20", "42"};
		Engine.insertRow("Test Table", values2);

		String[] values3 = {"Jingle", "24", "13"};
		Engine.insertRow("Test Table", values3);
	}

	public void deleteRow() {
		Engine.deleteRow("Test Table", "2413");
	}

	public void updateRow() {
		String[] values = {"Jimmy", "20", "01"};
		Engine.updateRow("Test Table", "2107", values);
	}

	public void show() {
		Engine.show("Test Table");
	}

 }
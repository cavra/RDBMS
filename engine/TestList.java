import java.util.*;

public class TestList {

	public void callAll() {

		// All functions should be called here
		createTable1();
		createTable2();
		insertRow1();
		insertRow2();
		show("Test Table");

		//deleteRow();
		//updateRow();
		//show("Test Table");

		//dropTable();
		//renameTable();
		//show("New Test Table");

		// None of these work
		//selection();
		//projection();
		//naturalJoin();
		//setUnion();
		//show("Set Union Table");
		//setDifference();
		//show("Set Difference Table");
		crossProduct();
		show("CP Table");

		//writeTable();
		//readTable();
	}

	public void createTable1() {

		String[] keys = {"name", "age", "jersey number"};
		String[] p_keys = {"age", "jersey number"};

		Engine.createTable("Test Table", keys, p_keys);
	}

	public void createTable2() {

		String[] keys = {"name", "age", "jersey number"};
		String[] p_keys = {"age", "jersey number"};

		Engine.createTable("Other Table", keys, p_keys);
	}

	public void dropTable() {
		Engine.dropTable("Test Table");
	}

	public void renameTable() {
		Engine.renameTable("Test Table", "New Test Table");
	}


	public void insertRow1() {

		String[] values1 = {"Matt", "90", "01"};
		Engine.insertRow("Test Table", values1);

		String[] values2 =  {"Jacob", "20", "42"};
		Engine.insertRow("Test Table", values2);

		String[] values3 = {"Jingle", "24", "13"};
		Engine.insertRow("Test Table", values3);
	}

	public void insertRow2() {

		String[] values1 = {"Jackson1", "21", "07"};
		Engine.insertRow("Other Table", values1);

		String[] values2 =  {"Jackson12", "100", "02"};
		Engine.insertRow("Other Table", values2);

		String[] values3 = {"Jackson13", "200", "05"};
		Engine.insertRow("Other Table", values3);
	}

	public void deleteRow() {
		Engine.deleteRow("Test Table", "2413");
	}

	public void updateRow() {
		String[] values = {"Jimmy", "20", "01"};
		Engine.updateRow("Test Table", "2107", values);
	}

	public void show(String table_name) {
		Engine.show(table_name);
	}

	public void selection(String table_name) {
		//Engine.selection(table_name);
	}

	public void projection(String table_name) {
		//Engine.projection(table_name);
	}

	public void naturalJoin(){
		Engine.naturalJoin("Test Table", "Other Table");
	}

	public void setUnion(){
		Engine.setUnion("Set Union Table", "Test Table", "Other Table");
	}

	public void setDifference(){
		Engine.setDifference("Set Difference Table", "Test Table", "Other Table");
	}

	public void crossProduct(){
		Engine.crossProduct("CP Table", "Test Table", "Other Table");
	}

	public void writeTable(){
		Engine.writeTable("Test Table");
	}

	public void readTable(){
		Engine.readTable("Test Table");
	}

 }
import java.util.*;

public class TestList {

	public void callAll() {

		// All functions should be called here
		createTable1();
		insertRow();
		show("Test Table");

		deleteRow();
		updateRow();
		show("Test Table");

		//dropTable();
		//renameTable();
		//show("New Test Table");

		// None of these work
		//selection();
		//projection();
		//naturalJoin();
		//setUnion();
		//setDifference();
		//crossProduct();

		writeTable();
		readTable();

	}

	public void createTable1() {

		String[] keys = {"name", "age", "jersey number"};
		String[] p_keys = {"age", "jersey number"};

		Engine.createTable("Test Table", keys, p_keys);
	}

	public void createTable2() {

		String[] keys = {"name", "age", "sport"};
		String[] p_keys = {"age", "sport"};

		Engine.createTable("Other Table", keys, p_keys);
	}

	public void dropTable() {
		Engine.dropTable("Test Table");
	}

	public void renameTable() {
		Engine.renameTable("Test Table", "New Test Table");
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

	public void show(String table_name) {
		Engine.show(table_name);
	}

	public void selection(String table_name) {
		Engine.selection(table_name);
	}

	public void projection(String table_name) {
		Engine.projection(table_name);
	}

	public void naturalJoin(){
		Engine.naturalJoin("Test Table", "Other Table");
	}

	public void setUnion(){
		Engine.setUnion("Set Union Table", "Test Table", "Other Table");
	}

	public void setDifference(){
		Engine.setDifference("Set Union Table", "Test Table", "Other Table");
	}

	public void crossProduct(){
		Engine.crossProduct("Test Table", "Other Table");
	}

	public void writeTable(){
		Engine.writeTable("Test Table");
	}

	public void readTable(){
		Engine.readTable("Test Table");
	}

 }
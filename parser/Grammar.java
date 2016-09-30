import java.util.*;
import java.io.*;

public class Grammar {

	Grammar(){}

// -----------------------------------------------------------------------------
// Queries
// -----------------------------------------------------------------------------

	void query(String name){}

	void relationName(String name){}

	void identifier(String name){}

	void alpha(String name){}

	void digit(String name){}

	void expr(String name){}

	void atomicExpr(String name){
		| selection
		| projection
		| renaming
		| union
		| difference
		| product
		| natural-join
	}

	void selection(String name){}

	void condition(String name){}

	void conjunction(String name){}

	void comparison(String name){}

	void op(String name){}

	void operand(String name){}

	void attributeName(String name){}

	void literal(String name){}

	void integer(String name){}

	void projection(String name){}

	void attributeList(String name){}

	void renaming(String name){}

	void union(String name){}

	void difference(String name){}

	void product(String name){}

	void naturalJoin(String name){}

// -----------------------------------------------------------------------------
// Commands
// -----------------------------------------------------------------------------

command ::= open-cmd | close-cmd | write-cmd | exit-cmd | show-cmd | create-cmd | update-cmd | insert-cmd | delete-cmd ;
open-cmd ::== OPEN relation-name
close-cmd ::== CLOSE relation-name
write-cmd ::== WRITE relation-name
exit-cmd ::== EXIT 
show-cmd ::== SHOW atomic-expr

//This command is DDL (Data Definition Language):

create-cmd ::= CREATE TABLE relation-name ( typed-attribute-list ) PRIMARY KEY ( attribute-list )
drop-cmd ::= DROP TABLE relation-name
typed-attribute-list ::= attribute-name type { , attribute-name type }
type ::= VARCHAR ( integer ) | INTEGER

//These commands are DML (Data Manipulation Language):

insert-cmd ::= INSERT INTO relation-name VALUES FROM ( literal { , literal } )
                       | INSERT INTO relation-name VALUES FROM RELATION expr
update-cmd ::= UPDATE relation-name SET attribute-name = literal { , attribute-name = literal } WHERE condition
delete-cmd ::= DELETE FROM relation-name WHERE condition

//Note that we made a distinction between queries and commands in the grammar. 
//The result of a query is a view. The result of a command is typically a boolean 
//value indicating if the operation was successful, with the exception that SHOW 
//prints the content of a relation into the console.
//A program is then defined as:

program ::= { query | command }

}


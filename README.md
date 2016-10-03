# CSCE-315 Group Project 1 - RDBMS

## Project Summary

**RDBMS** is the second programming assignment for CSCE 315 at Texas A&M, compromising of 3 parts: an engine, a parser, and an interactive system. See the DesignDocuments.pdf in the design_documents directory for detailed designs. Compare the development log below with the commit history for a detailed list of our meetings and productivity.

## Members
Jason Alonzo<br>
Cory Avra<br>
Reed Spivey<br>

## Instructions
Our program has been developed in compute.cs.tamu.edu, although it should run on all servers. We reccomend using compute. From the RDBMS directory, compile it with the makefile, and then execute it with 

$ java Parser < my_queries.txt 

Feel free to edit the my_queries.txt  

## Stories

Parser Functionality: 

- [x] Create Table
- [x] Drop Table
- [x] Insert Row
- [x] Update Row
- [x] Delete Row
- [x] Rename Table
- [x] Show
- [x] Selection
- [x] Projection
- [x] Set Union
- [x] Set Difference
- [x] Cross Product
- [x] Natural Join
- [x] Write Table
- [x] Read Table

Engine Functionality: 

- [x] Create Table
- [x] Drop Table
- [x] Insert Row
- [x] Update Row
- [x] Delete Row
- [x] Rename Table
- [x] Show
- [x] Selection
- [x] Projection
- [x] Set Union
- [x] Set Difference
- [x] Cross Product
- [x] Natural Join
- [x] Write Table
- [x] Read Table

Style:

- [x] Program follows typical Java convention as far as we are aware
- [x] Program is well documented with clear, concise comments

## Notes
This group project is only partially complete. As of right now, all we have is the engine and the parser. 

## Development Log

#### Week 1: Design Documents

**9/12/16**

Met at annex to develop understanding of project and discuss ideas

**9/14/16**

Collaborated together on how to approach project

**9/18/16**

Collaborated together on ALL ideas + intro of Design Document<br/>
Reed Spivey - Engine/Parser section of Design Document<br/>
Jason Alonzo - Engine/Parser section of Design Document<br/>
Cory Avra - Interactive System section of Design Document

#### Week 2: Engine

**9/19/16**

Creating Sport, Team, Player classes<br/>
Create a Array of Strings for each class

**9/21/16**

Created table class<br/>
Completed create table function<br/>
Removed Player, Team, and Sport Class<br/>
DBMS now represented as HashMap hashmap storing String Vectors, keyed with Strings<br/>
@TODO ID-Table class constructor<br/>
@TODO Work on insert function<br/>
@TODO Reed Spivey - Natural Join, Projection<br/>
      Jason Alonzo - Set Union, Set Difference<br/>
      Cory Avra - Write, Open, Close

**9/22/16**

Reed Spivey - Finished Natural Join function

**9/24/16**

Reed Spivey - Worked on Projection<br/>
Jason Alonzo - Worked on Set Union<br/>
Cory Avra - Write and read functions

**9/25/16**

Reed Spivey - Projection/cleaned code<br/>
Jason Alonzo - Set Union & Set difference/wrote print statements for successes<br/>
Cory Avra - Cross product, Testing, Re-wrote functions

#### Week 3: Parser

**9/26/16**

Reed Spivey - Worked on revising Design Documents<br/>
Jason Alonzo - Worked on testing framework<br/>
Cory Avra - Worked on cleaning code

**9/27/16**

Reed Spivey - Revised engine section of Design Documents, worked on Change Log<br/>
Jason Alonzo - Worked Change Log framework<br/>
Cory Avra - Revised Interactive System and Parser sections of Design Documents, worked on Change Log

**9/28/16**

Reed Spivey - Worked on recursive data structure design<br/>
Jason Alonzo - Worked on recursive data structure design<br/>
Cory Avra - Worked on regex for parser

**9/29/16**

Reed Spivey - Continued work on recursive data structure design<br/>
Jason Alonzo - Continued work on recursive data structure design<br/>
Cory Avra - Continued Worked on regex for parser

**10/02/16**

Reed Spivey - Continued work on recursive data structure design<br/>
Jason Alonzo - Continued work on recursive data structure design<br/>
Cory Avra - Revised regex designs, worked on a better implementation

**10/03/16**

Reed Spivey - Worked on various functions for parser<br/>
Jason Alonzo - Worked on various functions for parser<br/>
Cory Avra - Scrapped regex, now using tokenizer, Worked on various functions for parser

# CSCE-315 Group Project 1 - RDBMS

**RDBMS** is the second programming assignment for CSCE 315 at Texas A&M, compromising of 3 parts: an engine, a parser, and an interactive system. This is a team project by Cory Avra, Reed Spivey, and Jason Alonzo. See the DesignDocuments.pdf for detailed designs. Compare log.txt with the commit history for a detailed list of our meetings and productivity.

## Instructions
Our program has been developed in compute.cs.tamu.edu. Compile it with the makefile (which is in /engine), then execute the test code with 

$ java Engine

## Stories

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
This assignment is only partially complete. As of right now, all we have is the engine. Furthermore, we were unable to get JUnit, our testing framework, to work. So in the meantime, we have some sample tests in TestList.java. We will hope to have that fixed ASAP.

## Development Log

9/12/16
Met at annex to develop understanding of project and discuss ideas

9/14/16
Collaborated together on how to approach project

9/18/16
Collaborated together on ALL ideas + intro of Design Document
Reed Spivey - Engine/Parser section of Design Document
Jason Alonzo - Engine/Parser section of Design Document 
Cory Avra - Interactive System section of Design Document

9/9/16
Creating Sport, Team, Player classes
Create a Array of Strings for each class
HM<String uniqueID, HM<String,String>>

9/21/16
Created table class
Completed create table function
Removed Player, Team, and Sport Class
DBMS now represented as HashMap<String, ArrayList<Vector<String>>>
@TODO ID-Table class constructor
@TODO Work on insert function
@TODO Reed Spivey - Natural Join, Projection
      Jason Alonzo - Set Union, Set Difference
      Cory Avra - Write, Open, Close

9/22/16
Reed Spivey - Finished Natural Join function

9/24/16
Reed Spivey - Worked on Projection
Jason Alonzo - Worked on Set Union
Cory Avra - Write and read functions

9/25/16
Reed Spivey - Projection/cleaned code
Jason Alonzo - Set Union & Set difference/wrote print statements for successes
Cory Avra - Cross product, Testing, Re-wrote functions
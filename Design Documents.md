Design Document
===============

## Purpose of Project

Sports Management database project

## High Level Entities

* Engine

For the engine, specify what data structures you will use to represent relations (tables) in RAM. Note that for each relation, in addition to the data, you will also need to store the name and type of each column, and the name and type of the primary key. Also remember that your RDBMS must be generic -- it must NOT know about the schemas you use in your interactive system.

** purpose
** high level entities 
** low level design
*8 benefits/assumptions/risks/issues

* Parser

For the parser, explain your approach to parsing an input command, including detecting and dealing with erroneous input (error handling). Note that queries can be recursive, for example, one can do a renaming of a projection of a selection from a cross product. How do you deal with recursive structures? Hint: think of what data structures you have learned about that are recursive. Explain how the parser works together with the engine.

** purpose
** high level entities 
** low level design
*8 benefits/assumptions/risks/issues

* Interactive System

The interactive system itself will be of your own design, and the domain is open-ended: store, digital library, bank, web forum, warehouse app, auto maintenance shop, etc., but it must meet the following requirements:
It CANNOT BE one of the following: DVD or any kind of rental app.
It needs to be sufficiently complex. It should include at least three entities and two relations (a total of five relational tables, minimum). Thus, having a single table (e.g., a simple TODO list, etc.) is not acceptable.
The following command/query needs to be used at least once.
open, close, write, exit, and show
create table, insert into, update, and delete
select, project, +, - , and either * or JOIN (note: you have to think hard how to utilize all these in your interactive system)
The user experience should somehow be interesting and engaging. This requires sufficient complexity of the underlying database schema.

Include a list of user tasks you want to support in your interactive system, an ER diagram showing what entities and relationships are involved in order to support these user tasks, and an overview of system architecture (diagram(s) + explanation). Provide a rough sketch of the interface of your interactive system, and explain how the interface supports user tasks. Specify a draft of the API between the interactive system and the RDBMS. We will provide you with feedback in case it is too simple or too complex so that you can revise your plan early on.

** purpose
** high level entities 
** low level design
*8 benefits/assumptions/risks/issues


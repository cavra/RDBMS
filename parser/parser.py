#!/usr/bin/python 
import sys, re

def main():

	commands = []

	# Keep each non-empty line.
	for line in sys.stdin:
		if not line.strip():    
			commands.append(line)

	cases = { "create" : myFunction(),

	}  

	# Parse each line using regex and execute the corresponding class method.
	for command in commands:

		args = command # use regex here

		# Send the first argument to the case map
		cases[args[0]]() 

def myFunction():
	print("Hello World")

if __name__ == '__main__':
	main()
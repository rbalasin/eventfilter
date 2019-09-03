1.The starting point is com.mariner.filemerger.FileMerger, and the arguments are the file names. example: "com.mariner.filemerger.FileMerger reports.csv reports.xml reports.json".

2. I am using the following libraries for parsing the files (all the relevant libraries are in the "lib" folder. Please add these libraries to the build path)

	a. for Json, I am using gson-*.jar. It is developed and maintained by Google. I read that these libraries are very efficient.

	b. for csv, i use opencsv*.jar. I went thru some of the code. I find that it is also efficient as when writing a list to the file, they write the records to the file concurrently


Assumptions:

1. All input/output files are located in the same location(currentlty, "data" in the project is set to be the folder, and it can be changed in the main method of com.mariner.filemerger.FileMerger)

2. The output file is always a .csv file

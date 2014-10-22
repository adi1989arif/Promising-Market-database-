Promising-Market-database-
==========================

With the increasing market competition, the organizations have to consider various factors before setting up a rm in any area. To gain prot and recover what the organization has invested, they need to target large number of customers. We are trying to provide available population census data and the average growth of a particular area over the period to the organizations. This will enable the organization to decide where they want to set up their market.





For this project to execute correctly we need to first put the files in proper place. 
The .csv file and the java files stay in one directory.
If they are in a different directory then we need to modify the path when we add it to the DATABASE.

So first we do one time setup of mysql. We load the csv file into the database once and then we are free to run the program and see the output. 

We open mysql workbench.
My username is 'root' and passowrd is '1234'
create a database named 'bigdata'
and table called 'Metropolitan_Populations_20102012_'

The following queries need to be executed.

""""""
Create database bigdata


Use bigdata


Create table Metropolitan_Populations__20102012_(

	Geography varchar(500),
2010_Population int,
2011_Population int,
2012_Population int
);



LOAD DATA LOCAL INFILE 'C:\\Users\\v k shah\\Downloads\\Metropolitan_Populations__2010-2012_ (1).csv' 

INTO TABLE Metropolitan_Populations__20102012_ 

FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    
	LINES TERMINATED BY '\n'

	ignore 1 lines;
""""""

Once the database has been loaded we need to simply run the program.
The main method is in 'BigDataProject.java'
The database connection code is written in 'PromisingMarkets.java' under the init() method. 
Make sure you have the right username and password set to access the database.

The environment i used was eclipse on windows machine. 
We used the 'mysql-connector-java-5.0.8-bin.jar' as a JDBC connector plugin file.
We had to add it to my build path in order for it to run and successfully create the connection.
We believe you won't need to do anything but in case you have to, please make sure it is added to the build path.


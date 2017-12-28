//To connect to HP Vertica adjust private parameters in main.java, PathComputation.java and TranClosSpeedUpAttempt1.java file according to server. 

To compute transitive closures need to run: java main

All data computed will be stored in HP Vertica table name finalTable=EQU.

To compute path for given pair sourceVertex, destinationVertex,depth run: 
java PathComputation sourceVertex destinationVertex depth 
For convinience path will be stored in HP Vertica table PATH, but path computed only by using table EQU and as a one query request SELECT what gives opportunity store path in vertical form. 

To speed up process to compute transitive closures run file TranClosSpeedUpAttempt1.java.
This method not creating any intermidiate tables and send to server only one query that using ony initial table.

Statistics after run will be stored in file output.txt
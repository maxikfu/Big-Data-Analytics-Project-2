# Big-Data-Analytics-Project-2
To connect to HP Vertica adjust parameters in main.java and PathComputation.java file according to server. (this steps need to be changed)

To compute transitive closures need to run:
  java main. 
All data computed will be stored in HP Vertica table name finalTable.


To compute path for given pair sourceVertex, destinationVertex,depth run: 
  java PathComputation sour sourceVertexe destinationVertex depth
Path will  be stored in HP Vertica table PATH.

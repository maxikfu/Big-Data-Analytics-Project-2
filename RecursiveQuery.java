import java.util.*;


public class RecursiveQuery{
  private String initialTable;
  private String finalTable;
  private String intermidiateTablesBase;
  private Integer depth=0;
  public RecursiveQuery(String initialTableName, String finalTableName,
                String intermidiateTablesBaseName, Integer d, QueryMethods obj){
    initialTable = initialTableName;
    finalTable = finalTableName;
    intermidiateTablesBase = intermidiateTablesBaseName;
    depth = d;
    obj.executeQuery("DELETE FROM "+finalTable+";");
  }
  public void semiNaiveRecursion(QueryMethods object, String sourcevertexCondition){
    //creating temp E table. Exact copy of initial table with data
    String query = "CREATE TABLE E AS SELECT *, 1 AS p FROM "+initialTable+";";
    object.executeDropQuery("E","TABLE");
    object.executeQuery(query);
    //creating intermidiate bse tables R or watever
    String createTableR1="";
    String additionalColumns="";
    int j=2;
    while(j<=depth){//adding additional columns so we can union all later
      additionalColumns = additionalColumns + ", 0 as "+intermidiateTablesBase+j+"Edge ";
      j++;
    }

    createTableR1 = "CREATE TABLE "+intermidiateTablesBase+
                    "1 AS SELECT 1 AS d, i AS i, j AS j, p AS p, v AS v, E.i as R1Edge "
                    + additionalColumns + " FROM E " +sourcevertexCondition + " ORDER BY i,j,v";
    object.executeDropQuery(intermidiateTablesBase+"1", "TABLE");
    object.executeQuery(createTableR1);

    String prevEdges = intermidiateTablesBase+"1Edge, ";
		int d = 2;

    while (d <= depth) {
      String newTableName = intermidiateTablesBase + String.valueOf(d);
      String oldTableName = intermidiateTablesBase + String.valueOf(d - 1);
      String recursiveTableQuery = null;
      additionalColumns="";
			if (d<depth){
				j=d+1;
				while(j<=depth){//adding additional columns so we can union all later
					additionalColumns = additionalColumns + ", 0 as "+intermidiateTablesBase+j+"Edge";
					j++;
				}
			}
//path computation Maksim
      recursiveTableQuery = "CREATE TABLE " + newTableName
                + " AS SELECT "+ d + " AS d, " + oldTableName
                + ".i AS i, E.j AS j, " + oldTableName
                + ".p*E.p AS p," + "" + oldTableName + ".v+E.v AS v, "
                +prevEdges + "E.i as "+newTableName+"Edge"+additionalColumns
                + " FROM " + oldTableName
                + " JOIN E ON " + oldTableName + ".j=E.i WHERE "
                + oldTableName + ".i!=" + oldTableName + ".j ";
                prevEdges = prevEdges + newTableName+"Edge, ";
      recursiveTableQuery=recursiveTableQuery + " ORDER BY i,j,v";

      object.executeDropQuery(newTableName, "TABLE");
      object.executeQuery(recursiveTableQuery);
      String countQuery = "SELECT COUNT(*) FROM " + newTableName;
      //System.out.println("In table "+newTableName+ " was added " + object.executeStopQuery(countQuery)+" rows");
      if (object.executeStopQuery(countQuery) == 0) {
        break;
      }
      d++;
    }


    String createQuery = "INSERT INTO "+finalTable+" SELECT * FROM "+intermidiateTablesBase+"1";
    for(int i=2;i<d;i++) {
      createQuery=createQuery+" UNION ALL SELECT * FROM "+intermidiateTablesBase+String.valueOf(i);
    }


    //object.executeDropQuery(finalTable,"TABLE");
    object.executeQuery(createQuery);
    String rcountquery="SELECT COUNT(*) FROM "+finalTable;
    System.out.println("Number of rows in R table: "+object.executeStopQuery(rcountquery));
    //object.deleteIntermediateTables(d,intermidiateTablesBase);
  }
}

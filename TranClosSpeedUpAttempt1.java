import java.util.*;
import java.io.*;
//import java.System.*;

public class TranClosSpeedUpAttempt1{
  private static String ip = "127.0.0.1";
  private static String dbname = "gamma";
  private static String username = "team01";
  private static String password = "team01";
  public static String iniTable = "facebook_graph";
  public static String finalTable = "EQU";
  public static String intermidiateTablesBase = "R";
  public static Integer recursionDepth = 4;
  public static void main(String[] args) {
    try{
      PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
      System.setOut(out);
    }
    catch(IOException e1){
      System.out.println("Error during reading/writing file");
    }
    //creating connection and creating file where we are going to write every query executede in database
    QueryMethods methods = new QueryMethods(ip,dbname,username,password);
    //RecursiveQuery rec = new RecursiveQuery(iniTable,finalTable,intermidiateTablesBase,recursionDepth,methods);
    Long startTime = System.currentTimeMillis();
    String sourcevertexCondition = "";
    String finalQuery="";
    String createTableR1="";
    //creating R1
    createTableR1 = "SELECT 1 AS d, i AS i, j AS j, 1 AS p, v AS v, E.i as Edge,0 as LW "
                     + " FROM "+iniTable+" as E " +sourcevertexCondition + "";
    int d = 2;
    finalQuery = finalQuery + createTableR1;
    String orderBy = " ORDER BY i,j,v";
        //creating intermidiate tables
    String recursiveTableQuery =createTableR1;
    while (d <= recursionDepth) {
      String newTableName = intermidiateTablesBase + String.valueOf(d);
      String oldTableName = intermidiateTablesBase + String.valueOf(d - 1);
      finalQuery = finalQuery + " UNION ALL ";
      recursiveTableQuery = "SELECT "+ d + " AS d, " + oldTableName
                + ".i AS i, E.j AS j, " + oldTableName
                + ".p*1 AS p," + "" + oldTableName + ".v+E.v AS v, "
                + "E.i as Edge, E.v as LW"
                + " FROM "+"("+recursiveTableQuery+orderBy+") as "+ oldTableName
                + " JOIN "+iniTable+" as E ON " + oldTableName + ".j=E.i WHERE "
                + oldTableName + ".i!=" + oldTableName + ".j ";
      finalQuery = finalQuery + recursiveTableQuery;
      d++;
    }
    methods.executeDropQuery(finalTable, "TABLE");
    methods.executeQuery("CREATE TABLE "+finalTable+" AS "+finalQuery);
    Long elapTime = System.currentTimeMillis() - startTime;
    System.out.println(elapTime/1000F);
  }
}

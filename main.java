import java.util.*;
import java.io.*;
//import java.System.*;

public class main{
  private static String ip = "127.0.0.1";
  private static String dbname = "gamma";
  private static String username = "team01";
  private static String password = "team01";
  public static String iniTable = "facebook_graph";
  public static String finalTable = "EQU";
  public static String baseName = "R";
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
    RecursiveQuery rec = new RecursiveQuery(iniTable,finalTable,baseName,recursionDepth,methods);
    Long startTime = System.currentTimeMillis();
    rec.semiNaiveRecursion(methods,"");
    Long elapTime = System.currentTimeMillis() - startTime;
    System.out.println(elapTime/1000F);
    //PARTITION COMPUTATION
    //optimization by executing eachtime only for one sourcevertex
    /*long start = System.currentTimeMillis();
    Integer iter=1;
    ArrayList<String> listOfSourceVertexes = new  ArrayList<String>();
    listOfSourceVertexes = methods.executeQuery_Return("SELECT DISTINCT i FROM "+iniTable+";");
    System.out.println("Total itearations needed = "+listOfSourceVertexes.size());
    methods.executeQuery("DROP TABLE IF EXISTS "+finalTable+" CASCADE;");
    methods.executeQuery("CREATE TABLE "+finalTable+" (d integer,i integer,j integer,p integer,v integer, Edge integer,LW integer);");
    for(String obj:listOfSourceVertexes){
      System.out.println("Iteration= "+iter);
      iter = iter+1;
      rec.semiNaiveRecursion(methods,obj);
    }
    long elapsedTime = System.currentTimeMillis() - start;
    System.out.println("Total time = "+elapsedTime/1000F);*/
    }
}

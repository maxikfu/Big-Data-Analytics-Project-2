import java.util.*;
//import java.System.*;

public class main{
  private static String ip = "127.0.0.1";
  private static String dbname = "graph";
  private static String username = "dbadmin";
  private static String password = "1804";
  public static String iniTable = "small_graph";
  public static String finalTable = "EQU";
  public static String baseName = "R";
  public static Integer recursionDepth = 4;
  public static void main(String[] args) {
    //creating connection and creating file where we are going to write every query executede in database
    QueryMethods methods = new QueryMethods(ip,dbname,username,password);
    RecursiveQuery rec = new RecursiveQuery(iniTable,finalTable,baseName,recursionDepth,methods);
    Long startTime = System.currentTimeMillis();
    //rec.semiNaiveRecursion(methods,"");
    Long elapTime = System.currentTimeMillis() - startTime;
    System.out.println(elapTime/1000F);
    //optimization by executing eachtime only for one sourcevertex
    long start = System.currentTimeMillis();
    Integer iter=1;
    ArrayList<String> listOfSourceVertexes = new  ArrayList<String>();
    listOfSourceVertexes = methods.executeQuery_Return("SELECT DISTINCT i FROM "+iniTable+";");
    for(String obj:listOfSourceVertexes){
      System.out.println("Iteration= "+iter);
      iter = iter+1;
      rec.semiNaiveRecursion(methods,obj);
    }
    long elapsedTime = System.currentTimeMillis() - start;
    System.out.println(elapsedTime/1000F);
  }
}

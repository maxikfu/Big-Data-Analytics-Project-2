import java.util.*;
import java.io.*;
//import java.System.*;

public class PathComputation {
  private static String ip = "127.0.0.1";
  private static String dbname = "graphdb";
  private static String username = "maksim";
  private static String password = "maksim";
  public static String iniTable = "facebook_graph";
  public static String finalTable = "EQU";
  public static String baseName = "R";
  public static Integer recursionDepth = 4;
  public static String sourcevertex,destinationvertex,recDepth;
  public static void main(String[] args) {
    //creating connection and creating file where we are going to write every query executede in database
    QueryMethods methods = new QueryMethods(ip,dbname,username,password);
    long start = System.currentTimeMillis(); //sart time
    sourcevertex=args[0];
    destinationvertex=args[1];
    recDepth=args[2];

    String pathColumns="";
    Integer depth = Integer.parseInt(recDepth);
    Integer i = 2;
    while (i<depth){
      pathColumns = pathColumns+baseName+String.valueOf(i)+".Edge as e"+String.valueOf(i)+" ,";
      i=i+1;
    }
    String baseQuery = "select distinct "+finalTable+".d,"+finalTable+".i,"+pathColumns+finalTable+
    ".Edge as e"+String.valueOf(depth)+", "+finalTable+".j,"+finalTable+".p,"+finalTable+".v from "+finalTable+" ";

    String baseCondition = "  WHERE  "+finalTable+".i = "+sourcevertex+" and "+finalTable+".j = "+destinationvertex+
    " and "+finalTable+".d = "+recDepth+";";

    String leftJoin="";
    Integer d = depth-1;
    String weightCondition="";

    leftJoin = leftJoin + " LEFT JOIN (SELECT * FROM "+finalTable+" WHERE d = "+String.valueOf(d)+" and i = "+sourcevertex+
    " ) as R"+String.valueOf(d)+" on "+finalTable+".Edge = R"+String.valueOf(d)+".j and R"+String.valueOf(d)+
    ".v=("+finalTable+".v-"+finalTable+".LW)";

    d=d-1;
    while (d>0){
      weightCondition = weightCondition+"-R"+String.valueOf(d+1)+".LW";

      leftJoin = leftJoin + " LEFT JOIN (SELECT * FROM "+finalTable+" WHERE d = "+String.valueOf(d)+" and i = "+sourcevertex+
      " ) as R"+String.valueOf(d)+" on R"+String.valueOf(d+1)+".Edge = R"+String.valueOf(d)+".j and R"+String.valueOf(d)+
      ".v=("+finalTable+".v-"+finalTable+".LW"+weightCondition+")";

      d = d-1;
    }
    methods.executeDropQuery("PATH","TABLE");
    String query ="CREATE TABLE PATH as "+ baseQuery+leftJoin+baseCondition;
    methods.executeQuery(query);
  }


}

import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.sql.Statement;
import java.util.*;

public class QueryMethods{
  //executes any query
  protected Connection connection= null;
  protected PrintWriter sqlLogFile;
  //constructor where we make connection to database
  public QueryMethods(String ipaddres, String databaseName,
                                  String userName, String password)
    {
      try {
            connection  = DriverManager.getConnection(
                    "jdbc:vertica://"+ipaddres+":5433/"+databaseName,
                    userName,password);
            try {
              sqlLogFile = new PrintWriter(new FileWriter("Query_Log.sql"), true);
            } catch (IOException e) {
                      System.out.println("Error creating file: Query_Log.sql");
            }
            System.out.println("Connected");
            //conn.close();
    } catch (SQLTransientConnectionException connException) {
            System.out.print("Network connection issue: ");
            System.out.print(connException.getMessage());
            System.out.println(" Try again later!");
            return;
    } catch (SQLInvalidAuthorizationSpecException authException) {
            // Wrong log in credentials
            System.out.print("Could not log into database: ");
            System.out.print(authException.getMessage());
            System.out.println("Check the login credentials and try again.");
            return;
    } catch (SQLException e) {
            // Catch-all for other exceptions
            e.printStackTrace();
    }
  }

  public int executeStopQuery(String query) {
    try {
      Statement statement = connection.createStatement();
      //sqlLogFile.println(query);
      //sqlLogFile.println();
      ResultSet rs = statement.executeQuery(query);
      rs.next();
      return rs.getInt(1);


    } catch (Exception e) {
      System.out.println(e);
      System.out.println("Error while executing query " + query);
    }
    return -1;
  }


  public void executeQuery(String query) {
    try {
      Statement statement = connection.createStatement();
      //sqlLogFile.println(query);
      //sqlLogFile.println();
      statement.executeUpdate(query);

    } catch (Exception e) {
      System.out.println("Error while executing query " + query);
      System.out.println(e);
      System.exit(0);
    }
  }
  //execute drop querys for any type
  public void executeDropQuery(String tableName,String type) {
    try {
      Statement statement = connection.createStatement();
      String dropIfExistsQuery = "DROP "+type+" IF EXISTS " + tableName+" CASCADE";
      //sqlLogFile.println(dropIfExistsQuery);
      //sqlLogFile.println();
      statement.executeUpdate(dropIfExistsQuery);

    } catch (Exception e) {
      System.out.println("Error while executing drop query ");
      System.out.println(e);
      System.exit(0);
    }
  }

  //returns distinct sourcevertexes for every pair
  ArrayList<String> executeQuery_Return(String query) {
    ArrayList<String> output = new ArrayList<String>();
    try {
      Statement statement = connection.createStatement();
      //sqlLogFile.println(query);
      //sqlLogFile.println();
      ResultSet rs = statement.executeQuery(query);
      int x = 1;
      while(rs.next()){
        output.add("WHERE i = "+rs.getString(1).trim());
        x++;
      }

    } catch (Exception e) {
      System.out.println(e);
      System.out.println("Error while executing query " + query);
    }
    return output;
  }

  public void deleteIntermediateTables(int d, String intTableName) {
    executeDropQuery("E","TABLE");
    for (int i = 1; i < d; i++) {
      String tableName = intTableName + String.valueOf(i);
      executeDropQuery(tableName,"TABLE");
    }
  }
}

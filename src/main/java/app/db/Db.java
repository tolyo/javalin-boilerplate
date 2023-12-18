package app.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Db {
  private static Connection conn;

  public static void init() throws SQLException {
    // TODO extract from ENV vars
    String url = "jdbc:postgresql://localhost/postgres";
    Properties props = new Properties();
    props.setProperty("user", "postgres");
    props.setProperty("password", "postgres");
    conn = DriverManager.getConnection(url, props);
  }

  // Execute a query and return a single value
  public static <T> T queryVal(String query, Object... args) {
    T val = null;
    try {
      PreparedStatement statement = conn.prepareStatement(query);
      setStatementArguments(statement, args);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        val = (T) resultSet.getObject(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return val;
  }

  // Execute a query and return a list of values
  public static <T> List<T> queryList(String query, Object... args) {
    List<T> resultList = new ArrayList<>();
    try {
      PreparedStatement statement = conn.prepareStatement(query);
      setStatementArguments(statement, args);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        T item = (T) resultSet.getObject(1);
        resultList.add(item);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultList;
  }

  // Get the count of rows in a table
  public static int getCount(String tableName) {
    return queryVal("SELECT COUNT(*) FROM " + tableName);
  }

  // Delete all rows from a table
  public static void deleteAll(String tableName) {
    try {
      PreparedStatement statement = conn.prepareStatement("DELETE FROM " + tableName);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Set arguments for a prepared statement
  private static void setStatementArguments(PreparedStatement statement, Object... args)
      throws SQLException {
    for (int i = 0; i < args.length; i++) {
      statement.setObject(i + 1, args[i]);
    }
  }
}

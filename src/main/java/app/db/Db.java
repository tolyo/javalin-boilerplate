package app.db;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Db {
  private static Connection conn;

  public static void init() throws SQLException {
    // TODO extract from ENV vars
    String url = "jdbc:postgresql://localhost/default_db";
    Properties props = new Properties();
    props.setProperty("user", "postgres");
    props.setProperty("password", "postgres");
    conn = DriverManager.getConnection(url, props);
  }

  public static void close() {
    try {
      conn.close();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // Execute a query and return a single value
  public static <T> T queryVal(String query, Object... args) {
    T val = null;
    try {
      PreparedStatement statement = conn.prepareStatement(query);
      if (args != null) {
        setStatementArguments(statement, args);
      }
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
  public static void deleteAll(String tableName) throws SQLException {
    PreparedStatement statement = conn.prepareStatement("DELETE FROM " + tableName);
    statement.executeUpdate();
  }

  // Set arguments for a prepared statement
  private static void setStatementArguments(PreparedStatement statement, Object... args)
      throws SQLException {
    for (int i = 0; i < args.length; i++) {
      statement.setObject(i + 1, args[i]);
    }
  }

  public static <T> List<T> list(String tableName) {
    List<T> res = queryList("SELECT * FROM " + tableName);
    return res;
  }

  public static String create(String tableName, Object model)
      throws SQLException, IllegalAccessException {
    Field[] fields = model.getClass().getDeclaredFields();
    String query =
        String.format(
            "INSERT INTO %s (%s) VALUES (%s) RETURNING ID",
            tableName,
            Arrays.stream(fields)
                .map(x -> toSnakeCase(x.getName()))
                .collect(Collectors.joining(", ")),
            IntStream.range(0, fields.length).mapToObj(x -> "?").collect(Collectors.joining(", ")));

    PreparedStatement preparedStatement = conn.prepareStatement(query);
    int index = 1;
    for (Field f : fields) {
      Object value = f.get(model);
      if (value instanceof String) {
        preparedStatement.setString(index, (String) value);
      } else if (value instanceof Integer) {
        preparedStatement.setInt(index, (Integer) value);
      } else if (value instanceof Double) {
        preparedStatement.setDouble(index, (Double) value);
      } else if (value instanceof BigDecimal) {
        preparedStatement.setBigDecimal(index, (BigDecimal) value);
      } else {
        throw new RuntimeException("Unknown field type");
      }
      index++;
    }

    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getString(1);
    } else {
      return null;
    }
  }

  private static <T> Map<String, Object> convertObjectToMap(T obj) {
    Map<String, Object> params = new HashMap<>();

    try {
      Class<?> clazz = obj.getClass();
      Field[] fields = clazz.getDeclaredFields();

      for (Field field : fields) {
        field.setAccessible(true);
        params.put(field.getName(), field.get(obj));
      }
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return params;
  }

  // Convert field names to snake_case
  private static String toSnakeCase(String input) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char currentChar = input.charAt(i);
      if (Character.isUpperCase(currentChar)) {
        result.append("_").append(Character.toLowerCase(currentChar));
      } else {
        result.append(currentChar);
      }
    }
    return result.toString();
  }
}

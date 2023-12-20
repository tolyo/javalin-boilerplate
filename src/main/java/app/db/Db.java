package app.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
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

  public static <T> Optional<T> queryVal(Class<T> type, String query, Object... args)
      throws SQLException {
    try (PreparedStatement statement = conn.prepareStatement(query)) {
      setParameters(statement, args);
      try (ResultSet resultSet = statement.executeQuery()) {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
          columnNames[i - 1] = toCamelCase(metaData.getColumnName(i));
        }
        T val;
        Map<String, Object> res = new HashMap<>();
        if (resultSet.next()) {
          for (int i = 1; i <= columnCount; i++) {
            res.put(columnNames[i - 1], resultSet.getObject(i));
          }
          ObjectMapper objectMapper = new ObjectMapper();
          objectMapper.configure(
              com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
              false);
          val = objectMapper.convertValue(res, type);
          return Optional.of(val);
        }
      }
    }
    return Optional.empty();
  }

  public static <T> List<T> queryList(Class<T> type, String query, Object... args)
      throws SQLException {
    List<T> result = new ArrayList<>();
    try (PreparedStatement statement = conn.prepareStatement(query)) {
      setParameters(statement, args);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          result.add(resultSet.getObject(1, type));
        }
      }
    }
    return result;
  }

  public static Optional<Integer> getCount(Connection conn, String tableName)
      throws SQLException, InstantiationException, IllegalAccessException {
    return queryVal(Integer.class, "SELECT COUNT(*) FROM " + tableName);
  }

  public static void deleteAll(Connection conn, String tableName) throws SQLException {
    try (PreparedStatement statement = conn.prepareStatement("DELETE FROM " + tableName)) {
      statement.executeUpdate();
    }
  }

  private static void setParameters(PreparedStatement statement, Object... args)
      throws SQLException {
    for (int i = 0; i < args.length; i++) {
      statement.setObject(i + 1, args[i]);
    }
  }

  //
  //  // Execute a query and return a single value
  //  public static <T> T queryVal(String query, Object... args) {
  //    Map<String, Object> res = new HashMap<>();
  //    try {
  //      PreparedStatement statement = conn.prepareStatement(query);
  //      if (args != null) {
  //        setStatementArguments(statement, args);
  //      }
  //      ResultSet resultSet = statement.executeQuery();
  //      ResultSetMetaData metaData = resultSet.getMetaData();
  //      int columnCount = metaData.getColumnCount();
  //      String[] columnNames = new String[columnCount];
  //      for (int i = 1; i <= columnCount; i++) {
  //        columnNames[i - 1] = metaData.getColumnName(i);
  //      }
  //
  //      while (resultSet.next()) {
  //        for (int i = 1; i <= columnCount; i++) {
  //          res.put(columnNames[i-1], resultSet.getObject(i));
  //        }
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //    }
  //    return val;
  //  }
  //
  //  // Execute a query and return a list of values
  //  public static <T> List<T> queryList(String query, Object... args) {
  //    List<T> resultList = new ArrayList<>();
  //    try {
  //      PreparedStatement statement = conn.prepareStatement(query);
  //      int index = 1;
  //      for (Object i : args) {
  //        addCastedValue(statement, index, i);
  //        index++;
  //      }
  //      ResultSet resultSet = statement.executeQuery();
  //      while (resultSet.next()) {
  //        T item = (T) resultSet.getObject(1);
  //        resultList.add(item);
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //    }
  //    return resultList;
  //  }
  //
  //  // Get the count of rows in a table
  //  public static int getCount(String tableName) {
  //    return queryVal("SELECT COUNT(*) FROM " + tableName);
  //  }
  //
  //  // Delete all rows from a table
  //  public static void deleteAll(String tableName) throws SQLException {
  //    PreparedStatement statement = conn.prepareStatement("DELETE FROM " + tableName);
  //    statement.executeUpdate();
  //  }
  //
  //  // Set arguments for a prepared statement
  //  private static void setStatementArguments(PreparedStatement statement, Object... args)
  //      throws SQLException {
  //    for (int i = 0; i < args.length; i++) {
  //      statement.setObject(i + 1, args[i]);
  //    }
  //  }
  //
  //  public static <T> List<T> list(String tableName) {
  //    List<T> res = queryList("SELECT * FROM " + tableName);
  //    return res;
  //  }
  //
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
      addCastedValue(preparedStatement, index, value);
      index++;
    }

    ResultSet resultSet = preparedStatement.executeQuery();
    if (resultSet.next()) {
      return resultSet.getString(1);
    } else {
      return null;
    }
  }

  private static void addCastedValue(PreparedStatement preparedStatement, int index, Object value)
      throws SQLException {
    if (value instanceof String) {
      preparedStatement.setString(index, (String) value);
    } else if (value instanceof Integer || value instanceof BigInteger) {
      preparedStatement.setInt(index, (Integer) value);
    } else if (value instanceof Double) {
      preparedStatement.setDouble(index, (Double) value);
    } else if (value instanceof BigDecimal) {
      preparedStatement.setBigDecimal(index, (BigDecimal) value);
    } else {
      throw new RuntimeException("Unknown field type");
    }
  }

  //  private static <T> Map<String, Object> convertObjectToMap(T obj) {
  //    Map<String, Object> params = new HashMap<>();
  //
  //    try {
  //      Class<?> clazz = obj.getClass();
  //      Field[] fields = clazz.getDeclaredFields();
  //
  //      for (Field field : fields) {
  //        field.setAccessible(true);
  //        params.put(field.getName(), field.get(obj));
  //      }
  //    } catch (IllegalAccessException e) {
  //      e.printStackTrace();
  //    }
  //
  //    return params;
  //  }

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

  private static String toCamelCase(String input) {
    StringBuilder result = new StringBuilder();
    boolean capitalizeNext = false;

    for (int i = 0; i < input.length(); i++) {
      char currentChar = input.charAt(i);

      if (currentChar == '_') {
        capitalizeNext = true;
      } else {
        result.append(capitalizeNext ? Character.toUpperCase(currentChar) : currentChar);
        capitalizeNext = false;
      }
    }

    return result.toString();
  }
}

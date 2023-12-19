package app.db;

import static org.junit.jupiter.api.Assertions.assertNull;

import app.models.Product;
import java.math.BigDecimal;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DbTest {

  @BeforeAll
  static void setUp() throws SQLException {
    Db.init();
  }

  @AfterAll
  static void tearDown() throws SQLException {
    // Close the database connection
    Db.close();
  }

  @Test
  void testQueryValNoResult() {
    String result = Db.queryVal("SELECT title FROM products WHERE amount = 0.00");
    // Verify the result
    assertNull(result);
  }

  @Test
  void testInsert() throws SQLException, IllegalAccessException {
    String res =
        Db.create("products", new Product("test", "test.com", 1, BigDecimal.valueOf(100.00)));
    assertNotNull(res);
  }
}

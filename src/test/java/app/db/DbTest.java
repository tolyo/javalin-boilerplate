package app.db;

import static org.junit.jupiter.api.Assertions.*;

import app.models.Product;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DbTest {

  @BeforeAll
  static void setUp() throws SQLException {
    Db.init();
    // TODO create a temp 'foo' table with a sample Foo class just for this test to separate it away
    // from 'products'
  }

  @AfterAll
  static void tearDown() {
    Db.close();
  }

  @Test
  void testQueryValNoResult() {
    Optional result = Db.queryVal(String.class, "SELECT title FROM products WHERE amount = 0.00");

    assertTrue(result.isEmpty());
  }

  @Test
  void testQueryValResult() {
    Product example = createRandomProduct();
    String res = Db.create(example);

    assertNotNull(res);

    Product product =
        Db.queryVal(Product.class, "SELECT * FROM products WHERE id = ?", Integer.valueOf(res))
            .get();

    assertNotNull(product);
    assertEquals(product.title, example.title);
    assertEquals(product.imageUrl, example.imageUrl);
    assertEquals(product.price, example.price);
    assertEquals(product.amount, example.amount);
  }

  private Product createRandomProduct() {
    return new Product(
        String.valueOf(new Random().nextInt()), "test.com", 1, BigDecimal.valueOf(100.00));
  }

  @Test
  void testInsert() {
    String res = Db.create(new Product("test", "test.com", 1, BigDecimal.valueOf(100.00)));
    assertNotNull(res);
  }

  @Test
  void testqueryList() {
    Db.create(createRandomProduct());
    Db.create(createRandomProduct());
    Db.create(createRandomProduct());

    List<Product> products = Db.queryList(Product.class, "SELECT * FROM products");

    assertNotNull(products);
    assertTrue(products.size() >= 3);
  }

  @Test
  void testDelete() {
    String res = Db.create(new Product("test", "test.com", 1, BigDecimal.valueOf(100.00)));
    Optional result = Db.queryVal(Product.class, "SELECT * FROM products WHERE id = " + res);
    assertFalse(result.isEmpty());

    Db.delete("products", res);

    result = Db.queryVal(Product.class, "SELECT * FROM products WHERE id = " + res);
    assertTrue(result.isEmpty());
  }

  @Test
  void testUpdate() {
    String res = Db.create(new Product("test", "test.com", 1, BigDecimal.valueOf(100.00)));
    assertNotNull(res);
    Product product = Db.queryVal(Product.class, "SELECT * FROM products WHERE id = " + res).get();
    assertEquals(product.amount, 1);
    assertEquals(product.price, BigDecimal.valueOf(100.00));

    Db.update(res, new Product("test", "test.com", 12, BigDecimal.valueOf(120.00)));
    product = Db.queryVal(Product.class, "SELECT * FROM products WHERE id = " + res).get();
    assertEquals(product.amount, 12);
    assertEquals(product.price, BigDecimal.valueOf(120.00));
  }
}

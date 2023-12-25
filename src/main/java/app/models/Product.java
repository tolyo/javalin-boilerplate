package app.models;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Product {

  public BigInteger id;
  public String title;
  public String imageUrl;
  public Integer amount;
  public BigDecimal price;

  public Product() {}

  public Product(String title, String imageUrl, Integer amount, BigDecimal price) {
    this.title = title;
    this.imageUrl = imageUrl;
    this.amount = amount;
    this.price = price;
  }
}

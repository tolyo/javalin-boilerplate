package app.models;

import java.math.BigDecimal;

public class Product {
  public String title;
  public String imageUrl;
  public Integer amount;
  public BigDecimal price;

  public Product(String title, String imageUrl, Integer amount, BigDecimal price) {
    this.title = title;
    this.imageUrl = imageUrl;
    this.amount = amount;
    this.price = price;
  }
}

package app.models;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Product {

  public BigInteger id;
  @NotNull @NotBlank public String title;

  @NotNull @NotBlank public String imageUrl;

  @NotNull
  @Min(value = 0, message = "Invalid")
  public Integer amount;

  @NotNull
  @DecimalMin(value = "0.0", message = "Invalid")
  public BigDecimal price;

  public Product() {}

  public Product(String title, String imageUrl, Integer amount, BigDecimal price) {
    this.title = title;
    this.imageUrl = imageUrl;
    this.amount = amount;
    this.price = price;
  }
}

package web.product;

import app.models.Product;
import web.utils.ValidationHelper;

public class ProductValidator extends ValidationHelper<Product> {
  public ProductValidator(String body) {
    super(body, Product.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Product validate() {
    return (Product)
        this.check("title", notNullOrEmpty("title"), NULL_NOT_EMPTY_MESSAGE)
            .check("imageUrl", notNullOrEmpty("imageUrl"), NULL_NOT_EMPTY_MESSAGE)
            .check("amount", notNullOrEmpty("amount"), NULL_NOT_EMPTY_MESSAGE)
            .check("price", notNullOrEmpty("price"), NULL_NOT_EMPTY_MESSAGE)
            .get();
  }
}

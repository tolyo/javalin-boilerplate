package web;

import app.models.Product;
import web.product.ProductValidator;
import web.utils.CrudViewHandler;
import web.utils.ValidationHelper;

public class CrudProductController implements CrudViewHandler {
  @Override
  public Class getModelClass() {
    return Product.class;
  }

  @Override
  public ValidationHelper getCreateValidator(String body) {
    return new ProductValidator(body);
  }

  @Override
  public String getPath() {
    return "/products";
  }

  @Override
  public String getName() {
    return "products";
  }
}

package web.product;

import app.models.Product;
import web.utils.CrudViewHandler;
import web.utils.ValidationHelper;

public class ProductController extends CrudViewHandler {

  @Override
  public Class getModelClass() {
    return Product.class;
  }

  @Override
  public ValidationHelper getCreateValidator(String body) {
    return new ProductValidator(body);
  }
}

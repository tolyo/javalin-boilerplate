import { initRouter } from "./utils/router.js";
import getProductRoutes from "./product/product.js";
import "./utils/components.js";
import FormController from "./utils/form-controller.js";

// Boostrap AlpineJS
window.Alpine.start();
window.EventBus = new EventTarget();

document.addEventListener("DOMContentLoaded", () => {
  // Clean up an init all form controllers
  if (window.FormControllers) {
    window.FormControllers.forEach((i) => i.destroy());
  }
  window.FormControllers = [];
  document
    .querySelectorAll("form")
    .forEach((form) => window.FormControllers.push(new FormController(form)));
});

/**
 * @type {import("./utils/router.js").RouteConfig[]}
 */
const routes = window.routes.concat(getProductRoutes());

/**
 * Enable router if `ui-view` tag is present. Otherwise, fallback to default
 * browser routing/navigation.
 */
if (document.querySelector("ui-view") !== null) {
  initRouter(routes);
}

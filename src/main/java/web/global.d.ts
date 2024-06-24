import { StateService, UIRouter } from "@uirouter/core";
import FormController from "./utils/form-controller";
import { RouteConfig } from "./utils/router";

declare global {
  interface Window {
    router: UIRouter;
    routes: RouteConfig[];
    crudRoutes: RouteConfig[];
    stateService: StateService;
    EventBus: EventTarget;
    FormControllers: Array<FormController>;
  }
}

export {};

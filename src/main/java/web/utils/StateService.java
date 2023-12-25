package web.utils;

public class StateService {

  public static String list(String name) {
    return "stateService.go('" + name + "')";
  }

  public static String newAction(String name) {
    return "stateService.go('" + name + ":new')";
  }

  public static String created(String name) {
    return "(res) => {stateService.go('" + name + ":get', {'id': res.id})}";
  }

  public static String get(String name, String id) {
    return "stateService.go('" + name + ":get', {'id': " + id + "})";
  }

  public static String edit(String name, String id) {
    return "stateService.go('" + name + ":edit', {'id': " + id + "})";
  }
}

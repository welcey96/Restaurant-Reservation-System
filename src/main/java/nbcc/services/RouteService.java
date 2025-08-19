package nbcc.services;

public interface RouteService {
    void updateCurrentRoute(String url);
    String getCurrentRoute();
    void clearRouteSession();
}

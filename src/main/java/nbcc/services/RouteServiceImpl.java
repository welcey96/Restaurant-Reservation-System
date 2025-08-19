package nbcc.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class RouteServiceImpl implements RouteService {
    private static final String NAV_ROUTE = "navRoute";
    private static final String CURRENT_ROUTE = "currentRoute";
    private final HttpSession session;

    public RouteServiceImpl(HttpSession session) {
        this.session = session;
    }

    @Override
    public void updateCurrentRoute(String url) {
        var path = url.split("/");

        session.setAttribute(CURRENT_ROUTE, url);

        if (path.length > 0) {
            session.setAttribute(NAV_ROUTE,
                    path[1].equalsIgnoreCase("seatingtime") ? "event" : path[1]);
        } else {
            session.setAttribute(NAV_ROUTE, "event");
        }
    }

    @Override
    public String getCurrentRoute() {
        return (String) session.getAttribute(CURRENT_ROUTE);
    }

    @Override
    public void clearRouteSession() {
        session.removeAttribute(CURRENT_ROUTE);
        session.setAttribute(NAV_ROUTE, "events");
    }
}

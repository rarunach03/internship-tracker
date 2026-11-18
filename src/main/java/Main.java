import java.util.ArrayList;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        });
        app.get("/", ctx -> ctx.result("Hello"));
        app.start(8080);

        app.post("/applications", ctx -> {
            String company = ctx.formParam("company");
            String role = ctx.formParam("role");
            String status = ctx.formParam("status");
            String dateApplied = ctx.formParam("dateApplied");
            String followUpDate = ctx.formParam("followUpDate");

            ArrayList<Application> applications  = ApplicationStorage.load();
            
            Application newApp = new Application(company, role, status, dateApplied, followUpDate);

            applications.add(newApp);
            ApplicationStorage.save(applications);

            ctx.result("Saved!");
        });
    }
}
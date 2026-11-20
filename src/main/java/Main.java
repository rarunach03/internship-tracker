import java.util.ArrayList;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        });

        app.get("/", ctx -> {
            ArrayList<Application> apps = ApplicationStorage.load();
            
            if (apps.isEmpty()) {
                ctx.html("<p>No applications yet. <a href=\"/add.html\">Add one</a></p>");
                return;
            }

            StringBuilder html = new StringBuilder();
            html.append("<h1>My Applications</h1>");
            html.append("<table border=\"1\">");

            html.append("<tr>");
            html.append("<th>Company</th>");
            html.append("<th>Role</th>");
            html.append("<th>Status</th>");
            html.append("<th>Date Applied</th>");
            html.append("<th>Follow-up Date</th>");
            html.append("</tr>");

            for (Application a : apps) {
                html.append("<tr>");
                html.append("<td>").append(a.getCompany()).append("</td>");
                html.append("<td>").append(a.getRole()).append("</td>");
                html.append("<td>").append(a.getStatus()).append("</td>");
                html.append("<td>").append(a.getDateApplied()).append("</td>");
                html.append("<td>").append(a.getFollowUpDate()).append("</td>");
                html.append("</tr>");
            }

            html.append("</table>");
            html.append("<p><a href=\"/add.html\">Add application</a></p>");

            ctx.html(html.toString());
        });

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

            ctx.redirect("/");
        });

        app.start(8080);
    }
}
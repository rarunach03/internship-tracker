import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    static final String[] STATUSES = {"Applied", "Online Assessment", "Interviewing", "Offer", "Rejected"};
    static final int NO_RESPONSE_DAYS = 21;

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

            String q = ctx.queryParam("q");
            String searchText = (q == null) ? "" : q;

            String status = ctx.queryParam("status");
            String statusFilter = (status == null) ? "" : status;

            StringBuilder html = new StringBuilder();
            html.append("<h1>My Applications</h1>");
            html.append(buildAlertsHtml(buildAlerts(apps)));
            html.append(buildCountsHtml(buildCounts(apps)));
            html.append(buildFilterForm(searchText, statusFilter));
            html.append(buildTable(apps, searchText.trim().toLowerCase(), statusFilter));
            html.append("<p><a href=\"/add.html\">Add application</a></p>");

            ctx.html(html.toString());
        });

        app.post("/applications", ctx -> {
            String company = ctx.formParam("company");
            String role = ctx.formParam("role");
            String status = ctx.formParam("status");
            String dateApplied = ctx.formParam("dateApplied");
            String followUpDate = ctx.formParam("followUpDate");

            ArrayList<Application> applications = ApplicationStorage.load();
            applications.add(new Application(company, role, status, dateApplied, followUpDate));
            ApplicationStorage.save(applications);

            ctx.redirect("/");
        });

        app.post("/applications/update", ctx -> {
            int index = Integer.parseInt(ctx.formParam("index"));
            String newStatus = ctx.formParam("status");

            ArrayList<Application> applications = ApplicationStorage.load();
            applications.get(index).setStatus(newStatus);
            ApplicationStorage.save(applications);

            ctx.redirect("/");
        });

        app.start(8080);
    }

    static Map<String, Integer> buildCounts(ArrayList<Application> apps) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String s : STATUSES) {
            counts.put(s, 0);
        }
        for (Application a : apps) {
            String status = a.getStatus();
            if (counts.containsKey(status)) {
                counts.put(status, counts.get(status) + 1);
            }
        }
        return counts;
    }

    static String buildCountsHtml(Map<String, Integer> counts) {
        StringBuilder html = new StringBuilder("<ul>");
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            html.append("<li>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</li>");
        }
        return html.append("</ul>").toString();
    }

    static ArrayList<String> buildAlerts(ArrayList<Application> apps) {
        LocalDate today = LocalDate.now();
        ArrayList<String> alerts = new ArrayList<>();

        for (Application a : apps) {
            String status = a.getStatus();
            if (!"Applied".equals(status) && !"Online Assessment".equals(status)) {
                continue;
            }

            String label = a.getCompany() + " (" + a.getRole() + ")";

            String followUp = a.getFollowUpDate();
            if (followUp != null && !followUp.isEmpty()) {
                LocalDate followUpDate = LocalDate.parse(followUp);
                if (today.isAfter(followUpDate)) {
                    alerts.add(label + ": follow-up date passed on " + followUpDate);
                }
            }

            String applied = a.getDateApplied();
            if (applied != null && !applied.isEmpty()) {
                long daysSince = ChronoUnit.DAYS.between(LocalDate.parse(applied), today);
                if (daysSince > NO_RESPONSE_DAYS) {
                    alerts.add(label + ": no response in " + daysSince + " days");
                }
            }
        }
        return alerts;
    }

    static String buildAlertsHtml(ArrayList<String> alerts) {
        StringBuilder html = new StringBuilder("<h2>Alerts</h2>");
        if (alerts.isEmpty()) {
            return html.append("<p>No alerts</p>").toString();
        }
        html.append("<ul>");
        for (String alert : alerts) {
            html.append("<li>").append(escape(alert)).append("</li>");
        }
        return html.append("</ul>").toString();
    }

    static String buildFilterForm(String searchText, String statusFilter) {
        StringBuilder html = new StringBuilder();
        html.append("<form method=\"get\" action=\"/\">");
        html.append("<input type=\"text\" name=\"q\" placeholder=\"Search company or role\" value=\"")
            .append(escape(searchText)).append("\">");
        html.append("<select name=\"status\">");
        html.append("<option value=\"\">All statuses</option>");
        html.append(buildStatusOptions(statusFilter));
        html.append("</select>");
        html.append("<button type=\"submit\">Filter</button> ");
        html.append("<a href=\"/\">Clear</a>");
        return html.append("</form>").toString();
    }

    static boolean matchesFilter(Application a, String search, String statusFilter) {
        boolean matchesSearch = search.isEmpty()
            || a.getCompany().toLowerCase().contains(search)
            || a.getRole().toLowerCase().contains(search);
        boolean matchesStatus = statusFilter.isEmpty() || statusFilter.equals(a.getStatus());
        return matchesSearch && matchesStatus;
    }

    static String buildTable(ArrayList<Application> apps, String search, String statusFilter) {
        StringBuilder html = new StringBuilder("<table border=\"1\">");
        html.append("<tr><th>Company</th><th>Role</th><th>Status</th>")
            .append("<th>Date Applied</th><th>Follow-up Date</th></tr>");

        int shown = 0;
        for (int i = 0; i < apps.size(); i++) {
            Application a = apps.get(i);
            if (!matchesFilter(a, search, statusFilter)) {
                continue;
            }
            shown++;
            html.append(buildRow(a, i));
        }

        if (shown == 0) {
            html.append("<tr><td colspan=\"5\">No matching applications</td></tr>");
        }
        return html.append("</table>").toString();
    }

    static String buildRow(Application a, int index) {
        StringBuilder html = new StringBuilder("<tr>");
        html.append("<td>").append(escape(a.getCompany())).append("</td>");
        html.append("<td>").append(escape(a.getRole())).append("</td>");

        html.append("<td>");
        html.append("<form method=\"post\" action=\"/applications/update\">");
        html.append("<input type=\"hidden\" name=\"index\" value=\"").append(index).append("\">");
        html.append("<select name=\"status\">").append(buildStatusOptions(a.getStatus())).append("</select>");
        html.append("<button type=\"submit\">Update</button>");
        html.append("</form>");
        html.append("</td>");

        html.append("<td>").append(escape(a.getDateApplied())).append("</td>");
        html.append("<td>").append(escape(a.getFollowUpDate())).append("</td>");
        return html.append("</tr>").toString();
    }

    static String buildStatusOptions(String selected) {
        StringBuilder html = new StringBuilder();
        for (String s : STATUSES) {
            html.append("<option value=\"").append(s).append("\"");
            if (s.equals(selected)) {
                html.append(" selected");
            }
            html.append(">").append(s).append("</option>");
        }
        return html.toString();
    }

    static String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
public class Application {
    private String company;
    private String role;
    private String status;
    private String dateApplied;
    private String followUpDate;

    public Application(String company, String role, String status, String dateApplied, String followUpDate) {
        this.company = company;
        this.role = role;
        this.status = status;
        this.dateApplied = dateApplied;
        this.followUpDate = followUpDate;
    }

    public String getCompany() {
        return company;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getDateApplied() {
        return dateApplied;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateApplied(String dateApplied) {
        this.dateApplied = dateApplied;
    }

    public void setFollowUpDate(String followUpDate) {
        this.followUpDate = followUpDate;
    }
}
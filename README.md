# Internship Tracker

A simple Java web app for tracking internship applications, their stages, and when to follow up.

## The Problem

Applying to internships involves keeping track of many deadlines and following up on them, as it can get very confusing trying to monitor this information on a spreadsheet. In fact, I usually miss deadlines and forget to reply to recruiters because I forget to check my spreadsheet. While trackers like Simplify exist, they are more useful for those applying for full-time roles and usually have financial barriers.

## How it Works

I want to build a simple web app using Java, which allows me to record internship applications with details like company, role, date applied, status, and follow-up date that are all stored on a file. These applications will be updated as they go through the different stages, as it shows how many applications are in each stage. The app also provides alerts for applications that I have not heard back for a while so I know when to follow up.

**MVP features:**
- Add an application with company, role, date applied, status, and follow-up date
- Save applications to a file and load them when the app starts
- Update an application’s status as it moves through stages (ex: applied, interview, offer, rejected)
- Dashboard showing how many applications are in each stage
- Provide alerts for applications that go past the follow-up date or have no response after a certain number of days

![System Diagram](System%20Diagram.png)

Design files: [Eisenhower Matrix](Eisenhower%20Matrix.pdf) · [System Diagram source](System%20Diagram.drawio)

## Tech Stack

- **Languages:** Java, HTML, CSS, JavaScript
- **Libraries/Commands:** Javalin, Gson, Bootstrap, Chart.js, java.time
- **External Tools:** Maven, Git/GitHub, VS Code

## Setup

*(In progress. These steps will be finalized as the app is built.)*

```bash
git clone https://github.com/rarunach03/internship-tracker
cd internship-tracker
mvn compile
```

Requires Java 17+ and Maven.

## Usage

*(In progress.)* Run `Main.java`, then open `http://localhost:8080` in your browser to add and track applications.

## Roadmap

- [ ] Search, filter, and sort applications
- [ ] Stats page with response rate and applications per week
- [ ] Notes and recruiter contact fields
- [ ] Separate tracking for application deadlines

## Author

Rahul Arunachalam — [github.com/rarunach03](https://github.com/rarunach03)

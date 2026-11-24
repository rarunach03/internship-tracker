import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ApplicationStorage {

    private static final Path FILE = Path.of("applications.json");

    public static void save(ArrayList<Application> applications) {
        Gson gson = new Gson();
        String json = gson.toJson(applications);
        try {
            Files.writeString(FILE, json);
        } catch (IOException e) {
            System.err.println("Could not save applications: " + e.getMessage());
        }
    }

    public static ArrayList<Application> load() {
        // Start with an empty list if the file doesn't exist yet.
        if (!Files.exists(FILE)) {
            return new ArrayList<>();
        }
        try {
            String text = Files.readString(FILE);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Application>>(){}.getType();
            ArrayList<Application> applications = gson.fromJson(text, listType);
            // An empty file gives null, so return an empty list instead.
            return (applications == null) ? new ArrayList<>() : applications;
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Could not load applications: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
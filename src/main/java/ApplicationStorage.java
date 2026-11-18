import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApplicationStorage {
    public static void save(ArrayList<Application> applications) {
        Gson gson = new Gson();
        String json = gson.toJson(applications);
        try {
            Files.writeString(Path.of("applications.json"), json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Application> load() {
        try {
            String text = Files.readString(Path.of("applications.json"));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Application>>(){}.getType();
            ArrayList<Application> applications = gson.fromJson(text, listType);
            return applications;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.google.gson.Gson;

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
}
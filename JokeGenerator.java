import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class JokeGenerator {
    private static final String API_URL = "https://official-joke-api.appspot.com/random_joke";

    public static void main(String[] args) {
        System.out.println("=== Random Joke Generator ===\n");
        
        for (int i = 1; i <= 3; i++) {
            System.out.println("Joke " + i + ":");
            String joke = getRandomJoke();
            System.out.println(joke);
            System.out.println();
        }
    }

    public static String getRandomJoke() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String setup = jsonObject.getString("setup");
                String punchline = jsonObject.getString("punchline");

                return setup + "\n" + punchline;
            } else {
                return "Error fetching joke. Response code: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.Scanner;

public class AdvancedJokeGenerator {
    private static final String API_URL = "https://official-joke-api.appspot.com/random_joke";
    private static final String RANDOM_10_URL = "https://official-joke-api.appspot.com/jokes/random/10";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║    Advanced Joke Generator System      ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        while (running) {
            System.out.println("Choose an option:");
            System.out.println("1. Get a single random joke");
            System.out.println("2. Get 10 random jokes");
            System.out.println("3. Exit");
            System.out.print("Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Random Joke ---");
                    String joke = getRandomJoke();
                    System.out.println(joke);
                    System.out.println();
                    break;
                case 2:
                    System.out.println("\n--- 10 Random Jokes ---");
                    getMultipleJokes();
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Thank you for using the Joke Generator. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
        scanner.close();
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
                String type = jsonObject.getString("type");

                return "Type: " + type + "\n" + setup + "\n" + punchline;
            } else {
                return "Error fetching joke. Response code: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static void getMultipleJokes() {
        try {
            URL url = new URL(RANDOM_10_URL);
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

                String jsonArray = response.toString();
                jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
                String[] jokes = jsonArray.split("\\{"id":");

                for (int i = 0; i < jokes.length; i++) {
                    try {
                        String jokeStr = "{\"id\:" + jokes[i];
                        JSONObject jokeObj = new JSONObject(jokeStr.substring(0, jokeStr.lastIndexOf("}\" + 1));
                        System.out.println("\nJoke " + (i + 1) + ":");
                        System.out.println(jokeObj.getString("setup"));
                        System.out.println(jokeObj.getString("punchline"));
                    } catch (Exception e) {
                        // Skip malformed jokes
                    }
                }
            } else {
                System.out.println("Error fetching jokes. Response code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import jdk.internal.joptsimple.internal.Messages;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Locale;

/**
 * CHATBOT EXTENDS PIRCBOT(ABSTRACT CLASS)
 */
public class Chatbot extends PircBot{

    public Chatbot() {
        this.setName("myWeatherBot");
    }

    //connect to weather API
    public void connectAPI(String channel, String zipCode) throws IOException {
        //get URL
        URL url = new URL("https://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + "&units=imperial&APPID=26aa1d90a24c98fad4beaac70ddbf274");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        //read in the json
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        String jsonString = content.toString();

        JsonElement jelement = new JsonParser().parse(jsonString);

        //parse json
        JsonObject  jobject = jelement.getAsJsonObject();
        JsonObject name = jelement.getAsJsonObject();
        jobject = jobject.getAsJsonObject("main");
        JsonPrimitive temp = jobject.getAsJsonPrimitive("temp");

        //output the joke and temperature
        JsonElement city = name.get("name");
        String cityString = city.getAsString();
        cityString.replace("\"", "");
        sendMessage(channel, cityString + " is " + temp + " degrees..." +
                " The Programming Joke Is: " + connectJokeAPI(channel));

        in.close();
        con.disconnect();
    }

    public String connectJokeAPI(String channel) throws IOException {
        //get url
        URL url = new URL("https://v2.jokeapi.dev/joke/Programming");
        HttpURLConnection con = (HttpURLConnection)  url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        //get json
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        String jsonString = content.toString();

        //parson json
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonString);
        JsonObject object = element.getAsJsonObject();
        String joke = object.get("joke").getAsString();
        joke.replace("\"", "");
        return joke;//return the joke
    }

    /**
     * onMessage - will return message from the api
     * @returns void
     * @param channel - String
     * @param sender - String
     * @param login - String
     * @param hostname - String
     * @param message
     */

    public void onMessage(String channel, String sender, String login, String hostname, String message ){

        if(message.toLowerCase().contains("!weather ")){
            String zipCode = message.replace("!weather ", "");

            try {
                connectAPI(channel, zipCode);
            } catch (IOException e) {//if cant connect return error message
                e.printStackTrace();
                sendMessage(channel, "Sorry could not find this zip code try again make ");
            }
        }
    }
}

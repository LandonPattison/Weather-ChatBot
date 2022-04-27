//Landon Pattison
/*
This program implements pircbot and API's the first API is a weather API and the second is a joke API
ask the bot for the weather and it will give you the temperature for a certain zip code and
a joke to go along with it
*/

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.*;
import com.google.gson.*;
import java.util.Map;

public class ChatbotMain {
    public static void main(String[] args) throws Exception{

        //creates new bot
        Chatbot bot = new Chatbot();
        bot.setVerbose(true);
        bot.connect("irc.freenode.net");
        bot.joinChannel("#landonp");
        //send messaging when joining channel
        bot.sendMessage("#landonp", "Hey! Enter '!weather zipcode' to find the weather for given zip code (case sensitive), I will also tell you a programming joke each time you get the weather.");

    }
}

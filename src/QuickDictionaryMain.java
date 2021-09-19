/*
@author Emmett Willis
@date 9/16/2021
 */

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//Imports to access webpages and errors if pages are not found

public class QuickDictionaryMain {
    public static void main(String[] args){
        Scanner scnr = new Scanner(System.in);
        System.out.println("Input a single word to define" +
                "\n(names and proper nouns are case sensitive ie. hollywood should be Hollywood)");

        String wordToDefine = scnr.next();
        //Test definition, used to check and read specific sites, comment out later
        //testDefine(wordToDefine); // pass dictionary link into this test method
        //Call separate methods to search specific dictionary sites for word, return/print definitions
        merriamDefine(wordToDefine);
        dictComDefine(wordToDefine);//this function is not done but successfully calls the correct metadata
    }
    private static void testDefine(String dictSite){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dictSite))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
            ArrayList<String> scraper = new ArrayList<String>
                    (Arrays.asList(response.body().toString().split(">")));
            ArrayList<String> meta = new ArrayList<String>();
            /*for (int i = 0; i < scraper.size(); i++) {
                if (scraper.get(i).contains("meta")) meta.add(scraper.get(i));
            }
            for (int i = 0; i < meta.size(); i++) {
                System.out.println("meta found:" + meta.get(i));
            }*/
        } catch (IOException ex) {
            System.out.println("Website address not found");
        } catch (InterruptedException ex) {
            System.out.println("Website call interrupted");
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Website address not found");
        }
    }

    private static void merriamDefine(String wordToDefine){
        //narrow to metadata and find name="description"
        try {
            String dictSite = "https://www.merriam-webster.com/dictionary/" + wordToDefine;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dictSite))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.body());
            ArrayList<String> scraper = new ArrayList<String>
                    (Arrays.asList(response.body().toString().split(">")));
            int index = 0;
            while (index < scraper.size() - 1){
                if (!scraper.get(index).contains("name=\"description\"")){
                    scraper.remove(index);
                } else index++;
            }
            String defMeta = scraper.get(0).substring(scraper.get(0).indexOf("content=\"") + 8,
                    scraper.get(0).indexOf(" How to use " + wordToDefine)) + "\"";
            System.out.println("Merriam Webster Definition:");
            System.out.println(defMeta); //found meta tag with definition, break to only content

        } catch (IOException ex) {
            System.out.println("Website address not found");
        } catch (InterruptedException ex) {
            System.out.println("Website call interrupted");
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Definition not found");
        }
    }
    private static void dictComDefine(String wordToDefine){
        //narrow to metadata and find name="description"
        try {

            String dictSite = "https://www.dictionary.com/browse/" + wordToDefine.toLowerCase();
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(dictSite))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            //System.out.println(response.body());
            ArrayList<String> scraper = new ArrayList<String>
                    (Arrays.asList(response.body().toString().split(">")));
            int index = 0;
            while (index < scraper.size() - 1){
                if (!scraper.get(index).contains("name=\"description\"")){
                    scraper.remove(index);
                } else index++;
            }
            String defMeta = scraper.get(0).substring(scraper.get(0).indexOf("content=\"") + 8,
                    scraper.get(0).indexOf(" See more")) + "\"";
            //found meta tag with definition, break to only content
            System.out.println("Dictionary.com Definition:");
            System.out.println(defMeta);

        } catch (IOException ex) {
            System.out.println("Website address not found");
        } catch (InterruptedException ex) {
            System.out.println("Website call interrupted");
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("Definition not found");
        }
    }
}



package com.example.countries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

import java.util.HashMap;

@Service
public class RestClient {
    HashMap<String, Country> map = new HashMap<String, Country>();
    public void get() {
        try {

            // create HTTP Client
            HttpClient httpClient = HttpClientBuilder.create().build();

            // Create new getRequest with below mentioned URL
            HttpGet getRequest = new HttpGet("https://restcountries.eu/rest/v2/all?fields=name;capital;currencies");

            // Add additional header to getRequest which accepts application/xml data
            getRequest.addHeader("accept", "application/json");

            // Execute your request and catch response
            HttpResponse response = httpClient.execute(getRequest);

            // Check for HTTP response code: 200 = success
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            // Get-Capture Complete application/json body response
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String input;
            StringBuffer sresponse = new StringBuffer();
            System.out.println("============Output:============");

            // Simply iterate through JSON response and show on console.
            while ((input = br.readLine()) != null) {
                System.out.println(input);
                sresponse.append(input);

            }
            br.close();

            // https://www.chillyfacts.com/java-send-http-getpost-request-and-read-json-response/
            JSONArray myResponse = new JSONArray(sresponse.toString());
            for (int i = 0; i < myResponse.length(); i++) {
                JSONObject country = myResponse.getJSONObject(i);
                System.out.println("country " + country.toString());
                JSONArray currencies = country.getJSONArray("currencies");
                System.out.println("currencies " + currencies.toString());
                JSONObject currency = currencies.getJSONObject(0);
                System.out.println("currency " + currency.toString());
                String countryName = country.getString("name");
                if (countryName != null) {
                    String code = countryName.substring(0, 2).toUpperCase();
                    System.out.println("code " + code);
                    String capital = country.getString("capital");
                    String name = countryName;
                    Country newCountry = new Country(name, capital);
                    map.put(code, newCountry);
                }
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCodes() {
        if (map.size() == 0) {
            this.get();
        }

        String[] codes = map.keySet().toArray(new String[0]);
        JSONObject json = new JSONObject();
        json.put("country_codes", codes);
        return json.toString();
    }

    public String capital(String code) {
        if (map.size() == 0) {
            this.get();
        }

        if (map.containsKey(code)) {
            Country country = map.get(code);
            JSONObject json = new JSONObject();
            json.put("name", country.getName());
            json.put("capital", country.getCapital());
            return json.toString();
        }
        return new String(code + " does not exist");
    }
}
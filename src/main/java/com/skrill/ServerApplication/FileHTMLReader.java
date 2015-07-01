package com.skrill.ServerApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHTMLReader {

    public String getHTML(String path) {
        BufferedReader br = null;
        InputStreamReader fr = null;
        StringBuilder html = new StringBuilder();
        try {
            fr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path));
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                html.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html.toString();
    }

    public String getHTML(String path, String information) {
        BufferedReader br = null;
        InputStreamReader fr = null;
        StringBuilder html = new StringBuilder();
        try {
            fr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path));
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                html.append(line);
                if (line.contains("<div class=\"content\">")) {
                    html.append(information);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html.toString();
    }
}

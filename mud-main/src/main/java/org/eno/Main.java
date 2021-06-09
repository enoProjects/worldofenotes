package org.eno;

public class Main {

    public static void main(String args[]){
        System.out.println("hello world");
        System.setProperty("eno.credential.source","C:\\Users\\enotu\\Desktop\\my.properties");
        new TwitchConnection().run();

    }
}

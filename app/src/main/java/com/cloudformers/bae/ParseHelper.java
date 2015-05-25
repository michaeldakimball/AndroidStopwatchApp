package com.cloudformers.bae;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 5/22/2015.
 */
public class ParseHelper {
    public static final String email = "email";
    public static final String password = "pass";
    public static final String loginObj = "login";
    public static final String firstName = "fname";
    public static final String lastName = "lname";
    public static final String id = "objectId";
    public static final String userID = "uid";
    public static final String timesObj = "times";
    public static final String time = "time";
    public static ParseObject currentUser;
    private static boolean isUsername;
    private static boolean started = false;
    private static int timerCounter = 1;
    public static void startConnection(MainActivity context){
        // Enable Local Datastore.
        if(!started) {
            Parse.enableLocalDatastore(context);
            Parse.initialize(context, "NTW2RaUvjOZWPtYDuGXJN5cxqpqmDeLhFsm9hAgG", "r8SS3DGbcfsxiPMkFzEtNxU02rKgl42foEqQNbHV");
            started = true;
        }
    }
    public static String getCurrentId(){
        return currentUser.getObjectId();
    }
    public static boolean findLogin(ParseObject access){
        List<ParseObject> results = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(loginObj);
        query.whereEqualTo(email, access.get(email));
        query.whereEqualTo(password, access.get(password));
        try {
            results = query.find();
        }catch(ParseException e){
            Log.e("findLogin", e.getMessage());
        }finally{
            if(results == null){
                isUsername = false;
                currentUser = null;
            }else if(results.size() == 0){
                isUsername = false;
                currentUser = null;
            }else{
                isUsername = true;
                currentUser = results.get(0);
            }
        }
        return isUsername;
    }
    public static boolean findUsername(ParseObject access){
        List<ParseObject> results = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(loginObj);
        query.whereEqualTo(email, access.get(email));
        try {
            results = query.find();
        }catch(ParseException e){
            Log.e("findUsername", e.getMessage());
        }finally{
            if(results == null){
                isUsername = false;
            }else if(results.size() == 0){
                isUsername = false;
            }else{
                isUsername = true;
            }
        }
        return isUsername;
    }
    public static List<String> getTimes(){
        List<ParseObject> results = null;
        List<String> list = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(timesObj);
        query.whereEqualTo(userID, getCurrentId());
        try {
            results = query.find();
            while(results.size() > 0) {
                ParseObject current = results.remove(0);
                list.add("Time " + timerCounter++ + ": " + current.get(time).toString());
            }
        }catch(ParseException e){
            Log.e("getTimes", e.getMessage());
        }finally{
            return list;
        }
    }
    public static void deleteTime(int position){
        List<ParseObject> results = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(timesObj);
        query.whereEqualTo(userID, getCurrentId());
        try {
            results = query.find();
            ParseObject current = results.get(position);
            current.deleteInBackground();
        }catch(ParseException e){
            Log.e("getTimes", e.getMessage());
        }
    }
    public static int incrementCounter(){
        return timerCounter++;
    }
    public static void resetCounter(){
        timerCounter = 1;
    }
    private static void setIsUsername(boolean isUsername){
        ParseHelper.isUsername = isUsername;
    }
}

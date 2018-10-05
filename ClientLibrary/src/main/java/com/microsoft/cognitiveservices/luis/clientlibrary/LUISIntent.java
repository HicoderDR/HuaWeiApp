package com.microsoft.cognitiveservices.luis.clientlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes the LUIS Intent structure
 */
public class LUISIntent {

    private String name;
    private double score;
    private List<LUISAction> actions;

    /**
     * Constructs a LUIS Intention from a JSON object
     * @param JSONintent a JSONObject containing the intent data
     */
    public LUISIntent(JSONObject JSONintent) {
        name = JSONintent.optString("intent");
        score = JSONintent.optDouble("score");
        actions = new ArrayList<>();

        JSONArray JSONactions = JSONintent.optJSONArray("actions");

        for (int i = 0; JSONactions != null && i < JSONactions.length(); i++) {
            JSONObject JSONaction = JSONactions.optJSONObject(i);
            if(JSONaction != null) {
                LUISAction action = new LUISAction(JSONaction);
                actions.add(action);
            }
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"intent\":\"" + name + '\"' +
                ", \"score\":" + score +
                '}';
    }

    public String LUISActionList2String(){
        if (actions == null) return "";
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        LUISAction info = null;
        for (int i = 0; i < actions.size(); i++) {
            info = actions.get(i);
            jsonObject = new JSONObject();
            try {
                jsonObject.put("\"triggered\"", info.getTrigerred());
                jsonObject.put("\"name\"", info.getName());
                jsonObject.put("\"parameters\"",info.getParams());
            }
            catch (JSONException e){
                System.out.print("转化失败3");
            }
            array.put(jsonObject);
        }
        return array.toString();
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public List<LUISAction> getActions() {
        return actions;
    }

}

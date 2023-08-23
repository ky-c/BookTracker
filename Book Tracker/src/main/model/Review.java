package model;

import org.json.JSONObject;

// Represents a Review for something which contains:
// a rating out of 5, things liked, and things disliked
public class Review {

    private int rating;
    private String like;
    private String dislike;

    // REQUIRES: 0 <= rating <= 5
    // EFFECTS: creates a review with things liked,
    // disliked, and a rating out of 5
    public Review(String l, String d, int r) {
        rating = r;
        like = l;
        dislike = d;
    }

    // The following code is taken and modified from the Thingy class in the JsonSerializationDemo project:
    // https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/model/Thingy.java
    // EFFECTS: puts review into json
    public JSONObject toJson(JSONObject json) {
        json.put("like", like);
        json.put("dislike", dislike);
        json.put("rating", rating);
        return json;
    }

    public int getRating() {
        return rating;
    }

    public String getLike() {
        return like;
    }

    public String getDislike() {
        return dislike;
    }
}

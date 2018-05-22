package co.inanis.nearbyplaces.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Place {
    private String placeId;
    private String name;
    private double latitude;
    private double longitude;

    public Place() {
    }

    public Place(String placeId, String name, double latitude, double longitude) {
        this.placeId = placeId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static class PlaceDeserializer implements JsonDeserializer<Place> {

        @Override
        public Place deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject locationObject = jsonObject.get("geometry").getAsJsonObject().get("location").getAsJsonObject();

            return new Place(jsonObject.get("place_id").getAsString(),
                    jsonObject.get("name").getAsString(),
                    locationObject.get("lat").getAsDouble(),
                    locationObject.get("lng").getAsDouble());
        }
    }
}

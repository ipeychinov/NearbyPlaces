package co.inanis.nearbyplaces.model;

import android.location.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Place {
    private String placeId; // used in building the URI when opening the Maps app
    private String name;
    private Location location;
    private double distance;

    public Place() {
    }

    public Place(String placeId, String name, Location location, double distance) {
        this.placeId = placeId;
        this.name = name;
        this.location = location;
        this.distance = distance;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static class PlaceDeserializer implements JsonDeserializer<Place> {

        private Location mLastLocation;

        public PlaceDeserializer(Location lastLocation) {
            super();

            mLastLocation = lastLocation;
        }

        @Override
        public Place deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject locationObject = jsonObject.get("geometry").getAsJsonObject().get("location").getAsJsonObject();

            Location placeLocation = new Location("coordinates");
            placeLocation.setLatitude(locationObject.get("lat").getAsDouble());
            placeLocation.setLongitude(locationObject.get("lng").getAsDouble());

            return new Place(jsonObject.get("place_id").getAsString(),
                    jsonObject.get("name").getAsString(),
                    placeLocation,
                    mLastLocation.distanceTo(placeLocation) / 1000); //Distance in km
        }
    }
}

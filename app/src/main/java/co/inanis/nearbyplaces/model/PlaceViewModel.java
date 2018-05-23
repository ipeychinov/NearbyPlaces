package co.inanis.nearbyplaces.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Location;

import java.util.List;

public class PlaceViewModel extends ViewModel {
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();
    private final MutableLiveData<Location> location = new MutableLiveData<>();

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public void updatePlaces(List<Place> newPlaces) {
        places.setValue(newPlaces);
    }

    public MutableLiveData<Location> getLocation() {
        return location;
    }

    public void updateLocation(Location newLocation) {
        location.setValue(newLocation);
    }

    public boolean hasPlaceData() {
        return places.getValue() != null && !places.getValue().isEmpty();
    }
}

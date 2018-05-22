package co.inanis.nearbyplaces.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class PlaceViewModel extends ViewModel {
    private final MutableLiveData<List<Place>> places = new MutableLiveData<>();

    public LiveData<List<Place>> getPlaces() {
        return places;
    }

    public void updatePlaces(List<Place> newPlaces) {

        places.setValue(newPlaces);
    }

}

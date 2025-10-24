package dogapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    // Task 2: Complete this class
    private int callsMade = 0;
    private JSONObject cache = new JSONObject();
    public CachingBreedFetcher(BreedFetcher fetcher) {

    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // return statement included so that the starter code can compile and run.
        DogApiBreedFetcher fetcher = new DogApiBreedFetcher();
        String s = "";
        String url = "https://dog.ceo/api/breeds/list/all";

        if (cache.has(breed)) {
            List<String> subBreeds = new ArrayList<>();
            JSONArray subBreedsArray = cache.getJSONArray(breed);
            JSONArray subSubBreedsArray = subBreedsArray.getJSONArray(0);
            String str = subSubBreedsArray.toString();
            subBreeds = Arrays.asList(str.split(","));


            // Convert to ArrayList<String>
//            for (int i = 0; i < subBreedsArray.length(); i++) {
//                subBreeds.add(subBreedsArray.getString(i));
//            }
            return subBreeds;
        }
        else {

            try {
                s = fetcher.run(url);
                callsMade++;
            } catch (IOException e) {
            }

//            CachingBreedFetcher cachingBreedFetcher = new CachingBreedFetcher(this);
//            cachingBreedFetcher.getSubBreeds(breed);

            JSONObject json = new JSONObject(s);
            JSONObject breedMap = json.getJSONObject("message");

            if (breedMap.has(breed)) {
                List<String> subBreeds = new ArrayList<>();
                JSONArray subBreedsArray = breedMap.getJSONArray(breed);

                // Convert to ArrayList<String>
                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));
                }
                ArrayList<String> l = new ArrayList<>();
                cache.append(breed, l);
                for (int i = 0; i < subBreedsArray.length(); i++) {
                    l.add(subBreedsArray.getString(i));
                }
                return subBreeds;
            } else {
                throw new BreedNotFoundException(breed);
            }
        }
    }


    public int getCallsMade() {
        return callsMade;
    }
}
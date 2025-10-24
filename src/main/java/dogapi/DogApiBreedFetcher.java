package dogapi;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     *
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    final OkHttpClient client = new OkHttpClient();
    String url = "https://dog.ceo/api/breeds/list/all";
    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {

            DogApiBreedFetcher fetcher = new DogApiBreedFetcher();
            String s = "";

            try {
                s = fetcher.run(url);
            } catch (IOException e) {}

            CachingBreedFetcher cachingBreedFetcher = new CachingBreedFetcher(this);
            cachingBreedFetcher.getSubBreeds(breed);

            JSONObject json = new JSONObject(s);
            JSONObject breedMap = json.getJSONObject("message");

            if (breedMap.has(breed)) {
                List<String> subBreeds = new ArrayList<>();
                JSONArray subBreedsArray = breedMap.getJSONArray(breed);

                // Convert to ArrayList<String>
                for (int i = 0; i < subBreedsArray.length(); i++) {
                    subBreeds.add(subBreedsArray.getString(i));
                }
                return subBreeds;
            }
            else {
                throw new BreedNotFoundException(breed);
            }
//        List<String> sb = new ArrayList<>();
//        return sb;
        }
    }
//        try {
//            final JSONObject breeds = breedWithSubBreed.getJSONObject("message");
//
//            for (String breedName : breeds.keySet()) {
//                if (breedName.equals(breed)) {
//                    JSONArray subBreeds = breeds.getJSONArray(breedName);
//                    ArrayList<String> subBreedNames = new ArrayList<>();
//                    for (int i = 0; i < subBreeds.length(); i++) {
//                        subBreedNames.add(subBreeds.getJSONObject(i).toString());
//                    }
//                    return subBreedNames;
//                }
//            }
//            throw new BreedNotFoundException(breed);
//    }
//        catch (BreedNotFoundException b) {
//            throw new BreedNotFoundException(breed);
//            }
//        catch (IOException e) {}
//        List<String> names = new ArrayList<>();
//        return names;
//        }

//}
//
//        HttpUrl url = HttpUrl.parse(urlString);
//
//              Task 1: Complete this method based on its provided documentation
//              and the documentation for the dog.ceo API. You may find it helpful
//              to refer to the examples of using OkHttpClient from the last lab,
//              as well as the code for parsing JSON responses.
//         return statement included so that the starter code can compile and run.
//        return subBreeds;

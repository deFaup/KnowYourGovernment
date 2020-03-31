package com.gregoiredf.knowYourGovernment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class AsyncCivicDownload extends AsyncTask<Void, Void, String>
{
    private MainActivity mainActivity;
    private ArrayList<Office> officesList;
    private String GoogleCivicApiURL;

    AsyncCivicDownload(MainActivity mainActivity, ArrayList<Office> officesList, String location)
    {
        this.mainActivity = mainActivity;
        this.officesList = officesList;
        String headerURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
        String footerURL = String.format("&address=%s", location);
        GoogleCivicApiURL = String.format("%s%s%s", headerURL,
                mainActivity.getApplicationContext().getString(R.string.CIVIC_API_KEY), footerURL);
    }

    @Override
    protected String doInBackground(Void... voids)
    {
        JSONObject jsonObject = getDataOnStock(this.GoogleCivicApiURL);
        if (jsonObject == null)
            return null;

        // clear the existing list
        officesList.clear();

        // replace it with the new representatives
        String location = parseJson(jsonObject);
        return location;
    }

    @Override
    protected void onPostExecute(String location)
    {
        mainActivity.updateRecyclerView();
        mainActivity.updateLocation(location);
    }

    /* return a JSON Object with information from Google Civic API*/
    private JSONObject getDataOnStock(String url_) {
        JSONObject jsonObject = null;
        String urlToUse = Uri.parse(url_).toString();

        try {
            java.net.URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw (new Exception());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            jsonObject = new JSONObject(sb.toString());
            return jsonObject;
        }
        catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
    }

    /* parse the JSON to get representatives information and add those to the list */
    private String parseJson(JSONObject jsonObject)
    {
        try
        {
            String location = getLocation(jsonObject);

            JSONArray offices = jsonObject.getJSONArray("offices");
            JSONArray officials = jsonObject.getJSONArray("officials");

            // For each office in the offices JSON Array
            for(int i =0; i < offices.length(); ++i)
            {
                JSONObject office = offices.getJSONObject(i);

                // Get the name of the office
                String officeName = office.getString("name");

                // Get all people working in this office
                JSONArray officialIndices = office.getJSONArray("officialIndices");
                for (int j=0; j < officialIndices.length(); ++j)
                {
                    Office newOffice = new Office();
                    newOffice.setOfficeName(officeName);

                    int officialIndex = officialIndices.getInt(j);
                    JSONObject official = officials.getJSONObject(officialIndex);
                    setOfficial(official, newOffice);

                    // add the new representative to the list
                    officesList.add(newOffice);
                }
            }
            return location;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLocation(JSONObject jsonObject)
    {
        // Normalized input
        JSONObject normalizedInput = null;
        try {
            normalizedInput = jsonObject.getJSONObject("normalizedInput");
            StringBuilder address = new StringBuilder();
            address.append(normalizedInput.getString("city"));
            address.append(", ");
            address.append(normalizedInput.getString("state"));
            if(!normalizedInput.getString("zip").isEmpty())
            {
                address.append(", ");
                address.append(normalizedInput.getString("zip"));
            }
            return address.toString();
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void setOfficial(JSONObject official, Office newOffice)
    {
        try
        {
            /* Name */
            newOffice.setOfficialName(official.getString("name"));

            /* Address */
            if (!official.isNull("address"))
            {
                JSONObject addressObj = official.getJSONArray("address").getJSONObject(0);
                String address = addressObj.getString("line1");
                if (!addressObj.isNull("line2"))
                    address = address.concat(addressObj.getString("line2"));
                if (!addressObj.isNull("line3"))
                    address = address.concat(addressObj.getString("line3"));

                address = address.concat(", " + addressObj.getString("city"));
                address = address.concat(", " + addressObj.getString("state"));
                address = address.concat(", " + addressObj.getString("zip"));

                newOffice.setAddress(address);
            }
            /* Party */
            if (!official.isNull("party"))
                newOffice.setParty(official.getString("party"));

            if (!official.isNull("phones"))
                newOffice.setPhone(official.getJSONArray("phones").getString(0));

            if (!official.isNull("urls"))
                newOffice.setUrl(official.getJSONArray("urls").getString(0));

            if (!official.isNull("emails"))
                newOffice.setEmail(official.getJSONArray("emails").getString(0));

            if (!official.isNull("photoUrl"))
                newOffice.setImageURL(official.getString("photoUrl"));

            if (!official.isNull("channels"))
            {
                JSONArray channels = official.getJSONArray("channels");
                for (int i =0; i < channels.length(); ++i)
                {
                    JSONObject obj = channels.getJSONObject(i);
                    String key = obj.getString("type");
                    String value = obj.getString("id");
                    newOffice.addChannelPair(key, value);
                }
            }
        }

        catch (JSONException e) {e.printStackTrace();}
    }
}

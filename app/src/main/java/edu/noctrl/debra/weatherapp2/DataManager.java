package edu.noctrl.debra.weatherapp2;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Debra Jensen and Emily Huizenga on 4/29/16.
 */
public class DataManager {
    private WeatherInfoIO weatherIO;
    private WeatherInfo results;
    private boolean units = true; //boolean for units, initially true to indicate imperial mode
    private String zip; //string to store the zipcode
    private String[] coords = new String[2];

    public boolean getUnits()
    {
        return units;
    }

    public void setUnits(boolean u)
    {
        units = u;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String z)
    {
        zip = z;
    }

    public WeatherInfo getResults()
    {
        return results;
    }

   public void setupCurrent(AssetManager manager){
        weatherIO = new WeatherInfoIO();


        results = weatherIO.loadFromAsset(manager, zip + ".xml");

    }
    //add the current weather fragment


    public void getCoords(final Context ctx)
    {
        final String lat_long_url = "http://craiginsdev.com/zipcodes/findzip.php?zip=" + zip;
        Downloader<JSONObject> myDownloader = new Downloader<JSONObject>(new Downloader.DownloadListener<JSONObject>()
        {
            @Override
            public JSONObject parseResponse(InputStream in) throws IOException, JSONException {

                StringBuilder strBuild = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                //read lines from input
                String line = br.readLine();
                if(line == null)
                {
                    //make a toast saying bad Zip, the zip returned no data
                    Toast.makeText(ctx, R.string.badZipToast,
                            Toast.LENGTH_SHORT).show();
                    return null;
                }

                while(line != null){
                    strBuild.append(line);
                    line = br.readLine();
                }
                String result = strBuild.toString();
                JSONObject obj = new JSONObject(result);

                return obj;
            }

            @Override
            public void handleResult(JSONObject result) throws JSONException {
                coords[0] = result.getString("latitude");
                coords[1] = result.getString("longitude");
            }
        });
        myDownloader.execute(lat_long_url);
    }

}

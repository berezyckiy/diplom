package maks.dev.diplom.model.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maks.dev.diplom.base.GraphicResponseListener;

/**
 * Created by berezyckiy on 2/20/17.
 */

public class GraphicResponse extends AsyncTask<Void, Void, Boolean> {

    private GraphicResponseListener mListener;
    private String base;
    private String symbol;
    private ArrayList<Map<String, String>> data;

    public GraphicResponse(@NonNull GraphicResponseListener listener, String base, String symbol) {
        mListener = listener;
        this.base = base;
        this.symbol = symbol;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        data = new ArrayList<>();
        mListener.showProgressDialog();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            String url = "http://api.fixer.io/";
            HttpHandler hh = new HttpHandler();
            String jsonStr;
            HashMap<String, String> tmpMap;
            String year;
            for (int i = 0; i < 18; i++) {
                if (i < 10) {
                    year = "0" + i;
                } else {
                    year = "" + i;
                }
                jsonStr = hh.makeServiceCall(url + "20" + year + "-01-01" + "?base=" + base + "&symbols=" + symbol);
                tmpMap = new HashMap<>();
                if (jsonStr != null) {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject objRates = jsonObj.getJSONObject("rates");
                    tmpMap.put(symbol, objRates.isNull(symbol) ? "0" : objRates.getString(symbol));
                    data.add(tmpMap);
                } else {
                    tmpMap.put(symbol, "0");
                    data.add(tmpMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean) {
            mListener.onSuccessLoading(data);
        } else {
            mListener.onErrorLoading();
        }
        mListener.hideProgressDialog();
    }
}

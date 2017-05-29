package tech.wcdi.spajam.prejam0529.controllers.communications;

import android.os.AsyncTask;
import android.util.Log;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import tech.wcdi.spajam.prejam0529.config.Communication;
import tech.wcdi.spajam.prejam0529.controllers.IModel;

/**
 * Created by acq on 16/06/02.
 * Title:
 * Author:
 * Memo:
 * Todo:
 */
public class Rest extends AsyncTask<IModel, IModel, IModel> {
    private static final MediaType jsontype = MediaType.parse("application/json; charset=utf-8");
    private final String urlHosts = Communication.SCHEMA + "://" + Communication.DOMAIN;
    public String urlPath = "/index.html";
    // 入力データ群
    // 全データ
    public IModel allData;
    public OkHttpClient client = new OkHttpClient();
    public String url;
    // 受信したデータ
    IModel reply;

    public void setPath(String path) {
        this.url = this.urlHosts + path;
    }

    public IModel getReply() {
        return reply;
    }

    private String Post(String json) throws IOException {
        Log.d("TRANS/POST/START", "Post function start.");
        RequestBody body = RequestBody.create(jsontype, json);
        Request request = new Request.Builder().url(url).addHeader("Content-Language", "ja")

                .post(body).build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200) {
            Log.e("TRANS/POST/ERROR", "Responce code is " + response.code() + "!");
            return "{\"status\":false}";
        }
        Log.d("TRANS/POST/SUCCESS",
                "POST Successful! " + response.code() + "! " + response.body().contentLength());
        return response.body().string();
    }

    @Override
    protected IModel doInBackground(IModel... params) {
        try {
            Log.d("TRANSFER/START", "Download start...");
            Log.d("TRANSFER/START/URL", url);
            Log.d("TRANSFER/START/DATA", JSON.encode(allData));

            reply = JSON.decode(Post("q=" + JSON.encode(allData)), IModel.class);
        } catch (IOException e) {
            Log.e("TRANSFER/ERROR/EXCEPT", "IOException Error");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("TRANSFER/ERROR/EXCEPT", "JSONException Error");
            e.printStackTrace();
        }

        Log.d("TRANSFER/FINISH", "TRANSFER FINISHED.");
        return null;
    }
}

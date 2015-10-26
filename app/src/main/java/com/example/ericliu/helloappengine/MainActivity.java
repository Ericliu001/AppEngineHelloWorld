package com.example.ericliu.helloappengine;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ericliu.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnSayHi, btnSayGoodMorning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSayHi = (Button) findViewById(R.id.btnSayHi);
        btnSayGoodMorning = (Button) findViewById(R.id.btnSayGoodMorning);


        btnSayHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndpointsAsyncTask(new ApiAction() {
                    @Override
                    public String callApiMethod(MyApi myApi) {

                        try {
                            return myApi.sayHi("Eric").execute().getData();
                        } catch (IOException e) {
                            return e.getMessage();
                        }
                    }

                }).execute(new Pair<Context, String>(MainActivity.this, ""));
            }
        });


        btnSayGoodMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EndpointsAsyncTask(new ApiAction() {
                    @Override
                    public String callApiMethod(MyApi myApi) {

                        try {
                            return myApi.sayGoodMorning("Eric").execute().getData();
                        } catch (IOException e) {
                            return e.getMessage();
                        }
                    }
                }).execute(new Pair<Context, String>(MainActivity.this, ""));
            }
        });
    }


    interface ApiAction {
        String callApiMethod(MyApi myApi);
    }

    private static class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private MyApi myApi = null;
        private Context context;
        private ApiAction action;

        public EndpointsAsyncTask(ApiAction action) {
            this.action = action;
        }

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            if (myApi == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
//                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                            @Override
//                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                abstractGoogleClientRequest.setDisableGZipContent(true);
//                            }
//                        });
                        .setRootUrl("https://ericliu-hello-world.appspot.com/_ah/api/")
                        .setApplicationName("com.example.ericliu.helloappengine")
                ;
                myApi = builder.build();
            }
            context = params[0].first;
            String name = params[0].second;

            return action.callApiMethod(myApi);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }
}

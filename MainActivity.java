package com.data.krakowskismog;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Button aktualizuj;
    TextView napis;
    Button menu;
    Button zamknij;
    View pole;
    View main;
    RadioGroup wybor;
    TextView n1,n2,n3,n4,n5,n6,n7;
    TextView nso2,nno2,nco,no3,nc6h6;
    Switch szczegoly;
    private Stacje stacja;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stacja = new Stacje();
        napis = findViewById(R.id.napis);
        n1 = findViewById(R.id.n1);
        n2 = findViewById(R.id.n2);
        n3 = findViewById(R.id.n3);
        n4 = findViewById(R.id.n4);
        n5 = findViewById(R.id.n5);
        n6 = findViewById(R.id.n6);
        n7 = findViewById(R.id.n7);
        nso2 = findViewById(R.id.nso2);
        nno2 = findViewById(R.id.nno2);
        nco = findViewById(R.id.nco);
        no3 = findViewById(R.id.no3);
        nc6h6 = findViewById(R.id.nc6h6);
        try
        {
            //read chosen station id
            FileInputStream fIn = openFileInput("dane.txt");
            InputStreamReader isr = new InputStreamReader(fIn);
            int i;
            char c;
            String read="";
            while((i = isr.read())!=-1)
            {
                c = (char)i;
                read = read+c;
            }
            stacja.setId_stacji(Integer.valueOf(read));
            stacja.nowe_dane();
            isr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pole = findViewById(R.id.pole_menu);
        main = findViewById(R.id.main);
        menu = findViewById(R.id.menu);
        //station menu visibility
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.setVisibility(View.GONE);
                pole.setVisibility(View.VISIBLE);
            }
        });
        zamknij = findViewById(R.id.zamknij);
        zamknij.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pole.setVisibility(View.GONE);
                main.setVisibility(View.VISIBLE);
                if (szczegoly.isChecked())
                {
                    jsonParseSzczegoly();
                }else jsonParseStan();
            }
        });
        wybor = findViewById(R.id.wybor);
        //selecting station and setting its id
        wybor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FileOutputStream fOut = null;
                try {
                    fOut = openFileOutput("dane.txt", MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                OutputStreamWriter osw = new OutputStreamWriter(fOut);
                switch(checkedId)
                {
                    case R.id.w1:
                        stacja.zmiana(1);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w2:
                        stacja.zmiana(2);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w3:
                        stacja.zmiana(3);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w4:
                        stacja.zmiana(4);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w5:
                        stacja.zmiana(5);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w6:
                        stacja.zmiana(6);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w7:
                        stacja.zmiana(7);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.w8:
                        stacja.zmiana(8);
                        try {
                            osw.write((stacja.getId_stacji()).toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        szczegoly = findViewById(R.id.szczegoly);
        //basic or expand mode
        szczegoly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (szczegoly.isChecked())
                {
                    jsonParseSzczegoly();
                }else jsonParseStan();
            }

        });
        aktualizuj = findViewById(R.id.button);
        //
        aktualizuj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (szczegoly.isChecked())
                {
                    jsonParseSzczegoly();
                }else jsonParseStan();
            }
        });
        mQueue = Volley.newRequestQueue(this);
        jsonParseStan();
    }
    //send request and receive data
    private void jsonParseStan()
    {
        nso2.setText("dwutlenek siarki");
        nno2.setText("dwutlenek azotu");
        nco.setText("tlenek węgla");
        no3.setText("ozon");
        nc6h6.setText("benzen");
        String url = "http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/"+String.valueOf(stacja.getId_stacji());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //basic data
                try {
                    //JSONArray jsonArray = response.getJSONArray("stIndexLevel");
                    //JSONObject dane = jsonArray.getJSONObject(0);
                    JSONObject dane = response.getJSONObject("stIndexLevel");
                    String wartosc = dane.getString("indexLevelName");
                    switch(wartosc)
                    {
                        case "Bardzo dobry":napis.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":napis.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":napis.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":napis.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":napis.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":napis.setTextColor(Color.parseColor("#cc0000"));break;
                        case "Brak indeksu":napis.setTextColor(Color.parseColor("#262626"));
                            wartosc="brak danych";break;
                    }
                    napis.setText(wartosc);
                } catch (JSONException e) {
                    napis.setTextColor(Color.parseColor("#262626"));
                    napis.setText("brak danych");
                }
                //so2 data
                try {
                    JSONObject dane1 = response.getJSONObject("so2IndexLevel");
                    String wartosc1 = dane1.getString("indexLevelName");
                    switch(wartosc1)
                    {
                        case "Bardzo dobry":n1.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n1.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n1.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n1.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n1.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n1.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n1.setText(wartosc1);
                } catch (JSONException e) {
                    n1.setTextColor(Color.parseColor("#262626"));
                    n1.setText("brak danych");
                }
                //no2 data
                try{
                    JSONObject dane2 = response.getJSONObject("no2IndexLevel");
                    String wartosc2 = dane2.getString("indexLevelName");
                    switch(wartosc2)
                    {
                        case "Bardzo dobry":n2.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n2.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n2.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n2.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n2.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n2.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n2.setText(wartosc2);
                } catch (JSONException e) {
                    n2.setTextColor(Color.parseColor("#262626"));
                    n2.setText("brak danych");
                }
                //co data
                try {
                    JSONObject dane3 = response.getJSONObject("coIndexLevel");
                    String wartosc3 = dane3.getString("indexLevelName");
                    switch(wartosc3)
                    {
                        case "Bardzo dobry":n3.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n3.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n3.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n3.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n3.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n3.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n3.setText(wartosc3);
                } catch (JSONException e) {
                    n3.setTextColor(Color.parseColor("#262626"));
                    n3.setText("brak danych");
                }
                //pm10 data
                try {
                    JSONObject dane4 = response.getJSONObject("pm10IndexLevel");
                    String wartosc4 = dane4.getString("indexLevelName");
                    switch(wartosc4)
                    {
                        case "Bardzo dobry":n4.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n4.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n4.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n4.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n4.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n4.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n4.setText(wartosc4);
                } catch (JSONException e) {
                    n4.setTextColor(Color.parseColor("#262626"));
                    n4.setText("brak danych");
                }
                //pm25 data
                try {
                    JSONObject dane5 = response.getJSONObject("pm25IndexLevel");
                    String wartosc5 = dane5.getString("indexLevelName");
                    switch(wartosc5)
                    {
                        case "Bardzo dobry":n5.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n5.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n5.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n5.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n5.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n5.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n5.setText(wartosc5);
                } catch (JSONException e) {
                    n5.setTextColor(Color.parseColor("#262626"));
                    n5.setText("brak danych");
                }
                //o3 data
                try {
                    JSONObject dane6 = response.getJSONObject("o3IndexLevel");
                    String wartosc6 = dane6.getString("indexLevelName");
                    switch(wartosc6)
                    {
                        case "Bardzo dobry":n6.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n6.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n6.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n6.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n6.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n6.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n6.setText(wartosc6);
                } catch (JSONException e) {
                    n6.setTextColor(Color.parseColor("#262626"));
                    n6.setText("brak danych");
                }
                //c6h6 data
                try {
                    JSONObject dane7 = response.getJSONObject("c6h6IndexLevel");
                    String wartosc7 = dane7.getString("indexLevelName");
                    switch(wartosc7)
                    {
                        case "Bardzo dobry":n7.setTextColor(Color.parseColor("#00b33c"));break;
                        case "Dobry":n7.setTextColor(Color.parseColor("#33cc33"));break;
                        case "Umiarkowany":n7.setTextColor(Color.parseColor("#bfff00"));break;
                        case "Dostateczny":n7.setTextColor(Color.parseColor("#ffff00"));break;
                        case "Zły":n7.setTextColor(Color.parseColor("#ff3300"));break;
                        case "Bardzo zły":n7.setTextColor(Color.parseColor("#cc0000"));break;
                    }
                    n7.setText(wartosc7);
                } catch (JSONException e) {
                    n7.setTextColor(Color.parseColor("#262626"));
                    n7.setText("brak danych");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void jsonParseSzczegoly()
    {
        parseSo2();
        parseNo2();
        parseCo();
        parsePm10();
        parsePm25();
        parseO3();
        parseC6h6();
    }
    //expanded so2 data request
    private void parseSo2()
    {
        if(stacja.getId_so2() != 0) {
            nso2.setText("SO2");
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_so2());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n1.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n1.setTextColor(Color.parseColor("#e6e6e6"));
                        n1.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n1.setTextColor(Color.parseColor("#262626"));
                        n1.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            nso2.setText("SO2");
            n1.setTextColor(Color.parseColor("#262626"));
            n1.setText("brak danych");
        }
    }
    //expanded no2 data request
    private void parseNo2()
    {
        if(stacja.getId_no2() != 0) {
            nno2.setText("NO2");
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_no2());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n2.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n2.setTextColor(Color.parseColor("#e6e6e6"));
                        n2.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n2.setTextColor(Color.parseColor("#262626"));
                        n2.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            nno2.setText("NO2");
            n2.setTextColor(Color.parseColor("#262626"));
            n2.setText("brak danych");
        }
    }
    //expanded co data request
    private void parseCo()
    {
        if(stacja.getId_co() != 0) {
            nco.setText("CO");
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_co());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n3.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n3.setTextColor(Color.parseColor("#e6e6e6"));
                        n3.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n3.setTextColor(Color.parseColor("#262626"));
                        n3.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            nco.setText("CO");
            n3.setTextColor(Color.parseColor("#262626"));
            n3.setText("brak danych");
        }
    }
    //expanded pm10 data request
    private void parsePm10()
    {
        if(stacja.getId_pm10() != 0) {
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_pm10());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n4.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n4.setTextColor(Color.parseColor("#e6e6e6"));
                        n4.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n4.setTextColor(Color.parseColor("#262626"));
                        n4.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            n4.setTextColor(Color.parseColor("#262626"));
            n4.setText("brak danych");
        }
    }
    //expanded pm25 data request
    private void parsePm25()
    {
        if(stacja.getId_pm25() != 0) {
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_pm25());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n5.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n5.setTextColor(Color.parseColor("#e6e6e6"));
                        n5.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n5.setTextColor(Color.parseColor("#262626"));
                        n5.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            n5.setTextColor(Color.parseColor("#262626"));
            n5.setText("brak danych");
        }
    }
    //expanded o3 data request
    private void parseO3()
    {
        if(stacja.getId_o3() != 0) {
            no3.setText("O3");
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_o3());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n6.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n6.setTextColor(Color.parseColor("#e6e6e6"));
                        n6.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n6.setTextColor(Color.parseColor("#262626"));
                        n6.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            no3.setText("O3");
            n6.setTextColor(Color.parseColor("#262626"));
            n6.setText("brak danych");
        }
    }
    //expanded c6h6 data request
    private void parseC6h6()
    {
        if(stacja.getId_c6h6() != 0) {
            nc6h6.setText("C6H6");
            String url = "http://api.gios.gov.pl/pjp-api/rest/data/getData/" + String.valueOf(stacja.getId_c6h6());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("values");
                        JSONObject dane = jsonArray.getJSONObject(0);
                        n7.setTextColor(Color.parseColor("#CC000000"));
                        String wartosc = dane.getString("value");
                        if(wartosc == "null"){
                            dane = jsonArray.getJSONObject(1);
                            wartosc = dane.getString("value");
                            if(wartosc == "null")wartosc="0";
                        }
                        n7.setTextColor(Color.parseColor("#e6e6e6"));
                        n7.setText(String.format("%.4f", Double.parseDouble(wartosc))+" μg/m3");
                    } catch (JSONException e) {
                        n7.setTextColor(Color.parseColor("#262626"));
                        n7.setText("brak danych");
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mQueue.add(request);
        } else
        {
            nc6h6.setText("C6H6");
            n7.setTextColor(Color.parseColor("#262626"));
            n7.setText("brak danych");
        }
    }
}

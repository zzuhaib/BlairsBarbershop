package com.example.zuhaib.blairsbarbershop;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class DateTimeRequest extends StringRequest {
    private static final String DATETIME_REQUEST_URL = "http://blairbbb.epizy.com/DateTime.php";
    private Map<String, String> params;

        public DateTimeRequest(String name, String date, String time, Response.Listener<String> listener){
            super(Method.POST, DATETIME_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("name", name);
            params.put("date", date);
            params.put("time", time);
        }

        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Cookie", "__test=9b87af7aa785b2dff2dfd57ac40e3d59; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
            return headers;
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }

}

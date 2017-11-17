        package com.example.christian.christian_pset3;

        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.ImageRequest;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;

        public class MainActivity extends AppCompatActivity {

    TextView titleView, editButton, orderButton, descText, priceText;
    int layer_depth = 0;
    ListView listView;
    List<String> menuGroups = new ArrayList<String>();
    List<String> payArray = new ArrayList<String>();
    List<Integer> amountArray = new ArrayList<Integer>();
    List<Float> priceArray = new ArrayList<Float>();
    TextView mTxtDisplay;
    ArrayAdapter theAdapter;
    ImageView imageView;
    String path = "";
    String path2 = "";
    String payList = "";
    String priceList = "";
    String amountList = "";
    long selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        saveToSharedPrefs();
        loadFromSharedPrefs();

        imageView = findViewById(R.id.imageView);
        titleView = findViewById(R.id.titleView);
        editButton = findViewById(R.id.editButton);
        orderButton = findViewById(R.id.orderButton);
        listView = findViewById(R.id.listView);

        editButton.setVisibility(View.INVISIBLE);
        orderButton.setVisibility(View.INVISIBLE);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listView.setOnItemClickListener(new ClickSome());
        listView.setOnItemLongClickListener(new ClickSomeLong());

        mTxtDisplay = (TextView) findViewById(R.id.titleView);
        priceText = findViewById(R.id.priceView);
        descText = findViewById(R.id.descView);

        theAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuGroups);

        listView.setAdapter(theAdapter);

        menu();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    titleView.setText("Menu");
                    editButton.setVisibility(View.INVISIBLE);
                    orderButton.setVisibility(View.INVISIBLE);
                    if (layer_depth == 2)
                        switchCase(path2);
                    else
                        switchCase(path);
                    return true;
                case R.id.navigation_notifications:
                    yourOrder();
                    return true;
            }
            return false;
        }
    };

    public void yourOrder(){
        listView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        descText.setVisibility(View.INVISIBLE);
        priceText.setVisibility(View.INVISIBLE);
        String itemPicked = "Hold item to edit it!";
        Toast.makeText(MainActivity.this, itemPicked, Toast.LENGTH_SHORT).show();
        listView.setOnItemClickListener(null);
        menuGroups.clear();
        for (int i = 0; i < payArray.size(); i++) {
            menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + "x)");
        }
        theAdapter.notifyDataSetChanged();
        titleView.setText("Your order");
        editButton.setVisibility(View.INVISIBLE);
        orderButton.setVisibility(View.VISIBLE);
        if (payArray.size() == 0) {
            orderButton.setVisibility(View.INVISIBLE);
            descText.setVisibility(View.VISIBLE);
            descText.setText("No orders here yet!");
        }
        orderButton.setText("Order now!");
    }

    public void menu() {

        listView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        descText.setVisibility(View.INVISIBLE);
        priceText.setVisibility(View.INVISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/categories";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("categories");
                            menuGroups.clear();
                            for(int i = 0; i < group.length(); i++){
                                menuGroups.add(group.getString(i));

                            }
                            theAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            mTxtDisplay.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mTxtDisplay.setText("Eroor");

                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);


    }

    public void categories(final String category) {

        listView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        descText.setVisibility(View.INVISIBLE);
        priceText.setVisibility(View.INVISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");

                            menuGroups.clear();
                            for (int i = 0; i < group.length(); i++){
                                if (group.getJSONObject(i).getString("category").equals(category)) {
                                    menuGroups.add(group.getJSONObject(i).getString("name"));
                                }

                            }

                            theAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            mTxtDisplay.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        mTxtDisplay.setText("Eroor");

                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest);

    }

    public void dished(final String dish) {

        imageView.setImageResource(0);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");

                            menuGroups.clear();
                            for (int i = 0; i < group.length(); i++){
                                if (group.getJSONObject(i).getString("name").equals(dish)) {
                                    listView.setVisibility(View.INVISIBLE);

                                    descText.setVisibility(View.VISIBLE);
                                    descText.setText(group.getJSONObject(i).getString("description"));

                                    priceText.setVisibility(View.VISIBLE);
                                    priceText.setText("€ " + group.getJSONObject(i).getString("price"));

                                    imageRequestFunction(group.getJSONObject(i).getString("image_url"));
                                    imageView.setVisibility(View.VISIBLE);


                                }

                            }

                            theAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            mTxtDisplay.setText("did not work");
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mTxtDisplay.setText("Eroor");

                    }
                });

        // Access the RequestQueue
        queue.add(jsObjRequest);



    }

    public void editButtonClick(View view) {
        if (editButton.getText().equals("No, back!")) {
            yourOrder();
        }
        if (editButton.getText().equals("< back")) {
            layer_depth -= 1;
            switchCase(path);
        }
        else if (editButton.getText().equals("Delete this!")) {
            amountArray.set((int) selected, amountArray.get((int) selected) - 1);
            if (amountArray.get((int) selected) == 0){
                payArray.remove((int) selected);
                amountArray.remove((int) selected);
                priceArray.remove((int) selected);

            }
            orderButton.setVisibility(View.VISIBLE);
            if (payArray.size() == 0) {
                orderButton.setVisibility(View.INVISIBLE);
                descText.setVisibility(View.VISIBLE);
                descText.setText("No orders here yet!");
            }
            menuGroups.clear();
            for (int i = 0; i < payArray.size(); i++){
                menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + "x)");
            }
            theAdapter.notifyDataSetChanged();
            editButton.setVisibility(View.INVISIBLE);

            orderButton.setText("Order now!");
        }
        saveToSharedPrefs();

    }

    public void addToOrderClick(View view) {

            if (orderButton.getText().equals("Add to order")) {
            int index;
            boolean found = false;
            for (int i = 0; i < payArray.size(); i++){
                if (payArray.get(i).startsWith(titleView.getText().toString())) {
                    index = i;
                    amountArray.set(index, amountArray.get(i) + 1);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("HIIIIEEEER: " + payArray.toString());
                payArray.add(titleView.getText().toString());
                amountArray.add(1);
                priceArray.add(Float.valueOf(priceText.getText().toString().substring(2, priceText.getText().toString().length())));

            }

            layer_depth = 0;
            String itemPicked = "Added " + titleView.getText().toString() + " to order!";
            Toast.makeText(MainActivity.this, itemPicked, Toast.LENGTH_SHORT).show();
            switchCase("");


        }
        else if (orderButton.getText().equals("Add one of this!")){
            amountArray.set((int) selected, amountArray.get((int) selected) + 1);

            editButton.setVisibility(View.INVISIBLE);
            orderButton.setText("Order now!");
            menuGroups.clear();
            for (int i = 0; i < payArray.size(); i++){
                menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + "x)");
            }
            theAdapter.notifyDataSetChanged();

        }
        else if (orderButton.getText().equals("Order now!")) {
            listView.setVisibility(View.INVISIBLE);
            descText.setVisibility(View.VISIBLE);
            descText.setText("Are you sure you want to proceed to the order? (This cannot be undone!");
            float total_price = 0;
            for (int i = 0; i < priceArray.size(); i++) {

                total_price += Float.valueOf(priceArray.get(i)) * Float.valueOf(amountArray.get(i));
            }
            priceText.setVisibility(View.VISIBLE);
            priceText.setText("Total price: € " + total_price);
            orderButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            editButton.setText("No, back!");
            orderButton.setText("Yes, prep meal!");
        }
        else if (orderButton.getText().equals("Yes, prep meal!")) {
            waitTime();
        }

        menuGroups.clear();
        System.out.println("pretty" + payArray.toString() + amountArray.toString() + priceArray.toString());
        for (int i = 0; i < payArray.size(); i++){
            menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + "x)");
        }
        saveToSharedPrefs();
    }

    public void waitTime() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/order";

        JSONObject to_send = new JSONObject();

        try {
            to_send.put("menuIds", payArray.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, to_send, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            descText.setText("Your estimated waiting time is " + response.getString("preparation_time") + " minutes...");
                            priceText.setText("Thanx for your order!");
                            editButton.setVisibility(View.INVISIBLE);
                            orderButton.setVisibility(View.INVISIBLE);
                            titleView.setText("We're cookin'!");
                            menuGroups.clear();
                            payArray.clear();
                            priceArray.clear();
                            amountArray.clear();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        mTxtDisplay.setText("Error");

                    }
                });

        // Access the RequestQueue
        queue.add(jsObjRequest);
    }

    private class ClickSome implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            System.out.println(layer_depth);
            if (layer_depth < 2) {
                layer_depth += 1;
            }

            switchCase(String.valueOf(parent.getItemAtPosition(position)));

        }
    }

    private class ClickSomeLong implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if (titleView.getText().equals("Your order")) {
                editButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                editButton.setText("Delete this!");
                orderButton.setText("Add one of this!");
            }

            selected = id;

            return true;
        }
    }

    public void switchCase(String info) {
        switch(layer_depth) {
            case 0:
                path = "";
                editButton.setVisibility(View.INVISIBLE);
                orderButton.setVisibility(View.INVISIBLE);
                titleView.setText("Menu");
                listView.setOnItemClickListener(new ClickSome());
                menu();
                break;
            case 1:
                path = info;
                editButton.setVisibility(View.VISIBLE);
                editButton.setText("< back");
                orderButton.setVisibility(View.INVISIBLE);
                titleView.setText(info);
                listView.setOnItemClickListener(new ClickSome());
                categories(info);
                break;
            case 2:
                path2 = info;
                editButton.setVisibility(View.VISIBLE);
                editButton.setText("< back");
                orderButton.setVisibility(View.VISIBLE);
                orderButton.setText("Add to order");
                titleView.setText(info);
                dished(info);
                listView.setOnItemClickListener(null);
                break;
        }
    }

    public void imageRequestFunction(String imageUrl) {

        RequestQueue queue = Volley.newRequestQueue(this);
        // bron: https://www.programcreek.com/javi-api-examples/index.php?api=com.android.volley.toolbox.ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

        },
                0,
                0,
                null,
                Bitmap.Config.ALPHA_8,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No image available.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(imageRequest);
    }

    public void saveToSharedPrefs() {

        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        payList = payArray.toString();
        editor.putString("key1", payList);

        amountList = amountArray.toString();
        editor.putString("key2", amountList);

        priceList = priceArray.toString();
        editor.putString("key3", priceList);

        editor.commit();

    }

    public void loadFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("settings", this.MODE_PRIVATE);

        String received1 = prefs.getString("key1", null);
        if(received1 != null) {
            String convert1 = received1.substring(1,  received1.length() - 1);
            List<String> myList1 = new ArrayList<String>(Arrays.asList(convert1.split(",")));
            List<String>myList1string = new ArrayList<String>();
            for(String s : myList1) {
                System.out.println("dit " + s);
                if (s.length() > 0)
                    myList1string.add(s);
            }
            System.out.println(myList1.toString());
            payArray = myList1string;
        }

        String received2 = prefs.getString("key2", null);
        if(received2 != null) {
            String convert2 = received2.substring(1,  received2.length() - 1);
            List<String> myList2 = new ArrayList<String>(Arrays.asList(convert2.split(",")));
            List<Integer>myList2int = new ArrayList<Integer>();

            for(String s : myList2) {
                System.out.println("dits " + s);
                if (s.length() > 0)
                    myList2int.add(Integer.valueOf(s));
            }
            System.out.println(myList2.toString());
            amountArray = myList2int;
        }

        String received3 = prefs.getString("key3", null);
        if(received3 != null) {
            String convert3 = received3.substring(1,  received3.length() - 1);
            List<String> myList3 = new ArrayList<String>(Arrays.asList(convert3.split(",")));
            List<Float>myList3float = new ArrayList<Float>();
            for(String s : myList3) {
                System.out.println("dit " + s);
                if (s.length() > 0)
                    myList3float.add(Float.valueOf(s));
            }
            System.out.println(myList3.toString());
            priceArray = myList3float;
        }

    }
}


        package com.example.christian.christian_pset3;

        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.design.widget.BottomNavigationView;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView titleView, editButton, orderButton;
    int layer_depth = 0;
    ListView listView;
    List<String> menuGroups = new ArrayList<String>();
    List<String> payArray = new ArrayList<String>();
    List<Integer> amountArray = new ArrayList<Integer>();
    TextView mTxtDisplay;
    ArrayAdapter theAdapter;
    String[] orderArray = {"", ""};

    long selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    if (orderArray[1] != "")
                        switchCase(orderArray[1]);
                    else
                        switchCase(orderArray[0]);
                    return true;
                case R.id.navigation_notifications:
                    String itemPicked = "Hold item to edit it!";
                    Toast.makeText(MainActivity.this, itemPicked, Toast.LENGTH_SHORT).show();
                    listView.setOnItemClickListener(null);
                    menuGroups.clear();
                    for (int i = 0; i < payArray.size(); i++) {
                        menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + ")");
                    }
                    theAdapter.notifyDataSetChanged();
                    titleView.setText("Your order");
                    editButton.setVisibility(View.INVISIBLE);
                    orderButton.setVisibility(View.VISIBLE);
                    orderButton.setText("Order now!");
                    return true;
            }
            return false;
        }
    };


    public void menu() {

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

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>." + menuGroups);
                            menuGroups.clear();
                            for (int i = 0; i < group.length(); i++){
                                if (group.getJSONObject(i).getString("category").equals(category)) {
                                    menuGroups.add(group.getJSONObject(i).getString("name"));
                                }

                            }
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>." + menuGroups);
                            theAdapter.notifyDataSetChanged();

                            mTxtDisplay.setText(group.getJSONObject(4).getString("category"));

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

    public void dished(final String dish) {


        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://resto.mprog.nl/menu";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray group = response.getJSONArray("items");
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>." + menuGroups);
                            menuGroups.clear();
                            for (int i = 0; i < group.length(); i++){
                                if (group.getJSONObject(i).getString("name").equals(dish)) {
                                    menuGroups.add(group.getJSONObject(i).getString("name"));
                                    menuGroups.add(group.getJSONObject(i).getString("price"));
                                    menuGroups.add(group.getJSONObject(i).getString("description"));
                                }

                            }
                            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>." + menuGroups);
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
        if (editButton.getText().equals("< back")) {
            layer_depth -= 1;
            switchCase(orderArray[0]);
        }
        else if (editButton.getText().equals("Delete this!")) {

            amountArray.set((int) selected, amountArray.get((int) selected) - 1);
            if (amountArray.get((int) selected) == 0){
                payArray.remove((int) selected);
                amountArray.remove((int) selected);


            }
            menuGroups.clear();
            for (int i = 0; i < payArray.size(); i++){
                menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + ")");
            }
            theAdapter.notifyDataSetChanged();
            editButton.setVisibility(View.INVISIBLE);

            orderButton.setText("Order now!");
        }

    }

    public void addToOrderClick(View view) {
        System.out.println(amountArray.toString());
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
                payArray.add(titleView.getText().toString());
                amountArray.add(1);
            }

            layer_depth = 0;
            String itemPicked = "Added " + titleView.getText().toString() + " to order!";
            Toast.makeText(MainActivity.this, itemPicked, Toast.LENGTH_SHORT).show();
            switchCase("");


        }
        else if (orderButton.getText().equals("Add one of this!")){
            amountArray.set((int) selected, amountArray.get((int) selected) + 1);
            String temp;
            if (payArray.get((int) selected).endsWith(")"))
                temp = payArray.get((int) selected).substring(0, payArray.get((int) selected).length() - 4);
            else
                temp = payArray.get((int) selected);
            editButton.setVisibility(View.INVISIBLE);
            orderButton.setText("Order now!");
            menuGroups.clear();
            for (int i = 0; i < payArray.size(); i++){
                menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + ")");
            }
            theAdapter.notifyDataSetChanged();

        }
        else if (orderButton.getText().equals("Order now!")) {
//            REQUEST VOOR WAIT TIME
        }

        menuGroups.clear();
        for (int i = 0; i < payArray.size(); i++){
            menuGroups.add(payArray.get(i) + " (" + amountArray.get(i) + ")");
        }
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
                orderArray[0] = "";
                orderArray[1] = "";
                editButton.setVisibility(View.INVISIBLE);
                orderButton.setVisibility(View.INVISIBLE);
                titleView.setText("Menu");
                listView.setOnItemClickListener(new ClickSome());
                menu();
                break;
            case 1:
                orderArray[0] = info;
                orderArray[1] = "";
                editButton.setVisibility(View.VISIBLE);
                editButton.setText("< back");
                orderButton.setVisibility(View.INVISIBLE);
                titleView.setText(info);
                listView.setOnItemClickListener(new ClickSome());
                categories(info);
                break;
            case 2:
                orderArray[1] = info;
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

//    public String getJason(String url) {
//          LUKT NIET
//    }

}


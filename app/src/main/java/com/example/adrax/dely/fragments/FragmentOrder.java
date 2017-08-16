package com.example.adrax.dely.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.updateFace;
import static com.example.adrax.dely.MActivity.updateOrders;

//implements NoticeDialogFragment.NoticeDialogListener
public class FragmentOrder extends Fragment {

    EditText _fromText;
    EditText _toText;
    EditText _costText;
    EditText _padikText;
    EditText _codeText;
    EditText _floorText;
    EditText _koText;
    EditText _numText;
    EditText _recText;
    EditText _descriptionText;
    TextView _distanceView;
    TextView _payView;
    EditText _dayText;
    EditText _timeTakeText;
    EditText _timeBringText;
    Spinner _spinnerWeight;
    Calendar dayCalendar = Calendar.getInstance();
    Calendar takeCalendar = Calendar.getInstance();
    Calendar bringCalendar = Calendar.getInstance();
    Button button;

    String money = "";
    String distance = "";
    String weight_ = "500";

    boolean flagStart = false;
    public FragmentOrder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        _fromText = (EditText)root.findViewById(R.id.input_from);
        _toText = (EditText)root.findViewById(R.id.input_to);
        _costText = (EditText)root.findViewById(R.id.input_cost);
        _padikText = (EditText)root.findViewById(R.id.input_padik);
        _codeText = (EditText)root.findViewById(R.id.input_code);
        _floorText = (EditText)root.findViewById(R.id.input_floor);
        _koText = (EditText)root.findViewById(R.id.input_ko);
        _numText = (EditText)root.findViewById(R.id.input_num);
        _numText.setText(user.getPhone());
        _recText = (EditText)root.findViewById(R.id.input_recnum);
        _descriptionText = (EditText)root.findViewById(R.id.input_description);
        _payView = (TextView)root.findViewById(R.id.view_pay);
        _distanceView = (TextView) root.findViewById(R.id.input_distance);

        _distanceView.setText("5");
        _dayText = (EditText)root.findViewById(R.id.input_day);

        // FIDGET SPINNERS ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        _spinnerWeight = (Spinner) root.findViewById(R.id.spinner_weight);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.weights_array,
                        R.layout.item_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        _spinnerWeight.setAdapter(adapter);

        _spinnerWeight.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // An item was selected. You can retrieve the selected item using
                        // parent.getItemAtPosition(pos)
                        weight_ = _spinnerWeight.getSelectedItem().toString().substring(3,_spinnerWeight.getSelectedItem().toString().lastIndexOf("г"));
                        if (weight_.length()<3) weight_+="000";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                }
        );
        // End FIDGET ----------------------------------------------------

        // Pickers ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        // setup
        _timeTakeText = (EditText)root.findViewById(R.id.input_time_take);
        _timeBringText = (EditText)root.findViewById(R.id.input_time_bring);

        setChosenDay();
        _timeTakeText.setText(takeCalendar.get(Calendar.HOUR_OF_DAY)+":"+takeCalendar.get(Calendar.MINUTE));
        _timeBringText.setText(bringCalendar.get(Calendar.HOUR_OF_DAY)+":"+bringCalendar.get(Calendar.MINUTE));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                dayCalendar.set(Calendar.YEAR, year);
                dayCalendar.set(Calendar.MONTH, monthOfYear);
                dayCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setChosenDay();
            }
        };

        final TimePickerDialog.OnTimeSetListener timeTakeD = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                takeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                takeCalendar.set(Calendar.MINUTE, minute);
                _timeTakeText.setText(takeCalendar.get(Calendar.HOUR_OF_DAY)+":"+takeCalendar.get(Calendar.MINUTE));
            }
        };

        final TimePickerDialog.OnTimeSetListener timeBringD = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                bringCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                bringCalendar.set(Calendar.MINUTE, minute);
                _timeBringText.setText(bringCalendar.get(Calendar.HOUR_OF_DAY)+":"+bringCalendar.get(Calendar.MINUTE));
            }
        };

        _dayText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date,
                        dayCalendar.get(Calendar.YEAR),
                        dayCalendar.get(Calendar.MONTH),
                        dayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        _timeTakeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeTakeD,
                        takeCalendar.get(Calendar.HOUR_OF_DAY),
                        takeCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        _timeBringText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeBringD,
                        bringCalendar.get(Calendar.HOUR_OF_DAY),
                        bringCalendar.get(Calendar.MINUTE), true).show();
            }
        });
        // End pickers ----------------------------------------------------------

        button = (Button) root.findViewById(R.id.btn_order);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClicked();
            }
        });

        return root;
    }

    private void setChosenDay() {
        String myFormat = "d MMM yyyy";
        //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        Locale russian = new Locale("ru");
        String[] newMonths = {
                "января", "февраля", "марта", "апреля", "мая", "июня",
                "июля", "августа", "сентября", "октября", "ноября", "декабря"};
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(russian);
        dfs.setMonths(newMonths);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, russian);
        SimpleDateFormat sdf = (SimpleDateFormat) df;
        sdf.setDateFormatSymbols(dfs);

        _dayText.setText(sdf.format(dayCalendar.getTime()));
    }

    // Get запрос к гуглу, получение дистанции
    public void HuyakGoogleRequest(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = null;
        try {
            url = String.format("https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=%1$s&" +
                    "destination=%2$s&" +
                    "mode=driving&" +
                    "language=ru&" +
                    "unit=metric&" +
                    "region=ru&" +
                    "key=AIzaSyCOn2sDKTCQfGZfT3QTO99snxEikwrZkQ4", URLEncoder.encode("Пермь,"+_fromText.getText().toString(),"UTF-8"), URLEncoder.encode("Пермь,"+_toText.getText().toString(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //response.getJSONObject("routes").getJSONObject("legs").getJSONObject("distance").getString("value");
                            distance = response.getJSONArray("routes").getJSONObject(0)
                                    .getJSONArray("legs").getJSONObject(0)
                                    .getJSONObject("distance").getString("value");
                            _distanceView.setText(distance + "м");
                            flagStart = true;
                        }
                        catch ( Exception e){
                            // ToDo: заменить на уведомления об ошибке
                            _distanceView.setText("Error: "+e.toString());//"Мы не можем найти указанные адреса");
                            flagStart = false;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // ToDo: заменить на уведомления об ошибке
                _payView.setText("Error: "+error.toString());
                flagStart = false;
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // ToDo: Do something with this high quality code below

    ArrayList<ArrayList<String>> price_list = new ArrayList<ArrayList<String>>();

    // Билдер коллекции цен
    public void HuyakArray(){
        ArrayList<String> list =  new ArrayList<String>();
        list.add("50");
        list.add("90");
        list.add("170");
        list.add("270");
        list.add("420");
        price_list.add(list);
        list =  new ArrayList<String>();
        list.add("60");
        list.add("100");
        list.add("180");
        list.add("290");
        list.add("430");
        price_list.add(list);
        list =  new ArrayList<String>();
        list.add("70");
        list.add("110");
        list.add("190");
        list.add("310");
        list.add("450");
        price_list.add(list);
        list =  new ArrayList<String>();
        list.add("80");
        list.add("130");
        list.add("210");
        list.add("330");
        list.add("480");
        price_list.add(list);
        list =  new ArrayList<String>();
        list.add("100");
        list.add("150");
        list.add("230");
        list.add("350");
        list.add("500");
        price_list.add(list);
    }

            /*[[50, 90, 170, 270, 420],
            [60, 100, 180, 290, 430],
            [70, 110, 190, 310, 450],
            [80, 130, 210, 330, 480],
            [100, 150, 230, 350, 500]]
            */

    // Считает деньги по формуле
    public String Cash()
    {
        int mass = Integer.parseInt(weight_);
        int length = Integer.parseInt(distance);
        HuyakArray();
        int y;
        int x;
             if  (mass <= 500) y = 0;
        else if (mass <=  1000 ) y = 1;
        else if (mass <=  2000 ) y = 2;
        else if (mass <=  5000 ) y = 3;
        else y = 4;

             if (length <= 5000 ) x = 0;
        else if (length <= 10000) x = 1;
        else if (length <= 15000) x = 2;
        else if (length <= 20000) x = 3;
        else x = 4;

        money = price_list.get(x).get(y);

        return price_list.get(x).get(y);
    }

    // Призыв даилога
    public void buttonClicked () {

        // ХХДД
        HuyakGoogleRequest(); // Узнаём расстояние и флаг
        if (flagStart) {
            _payView.setText(Cash() + "руб.");

            OrderDialog orderDialog = new OrderDialog();
            Bundle args = new Bundle();
            args.putString("money", money);
            args.putString("timeTake", _timeTakeText.getText().toString());
            args.putString("timeBring", _timeBringText.getText().toString());
            args.putString("from", _fromText.getText().toString());
            args.putString("to", _toText.getText().toString());
            args.putString("number", _numText.getText().toString());
            args.putString("description", _descriptionText.getText().toString());
            args.putString("cost", _costText.getText().toString());
            args.putString("weight", weight_);
            args.putString("day",_dayText.getText().toString());
            orderDialog.setArguments(args);
            orderDialog.setTargetFragment(this, 0);
            orderDialog.show(getFragmentManager(), "OrderDialog");
            flagStart = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            SendOrder();
    }

    // Отправка формы на сервер
    public void  SendOrder()
    {
        final String from = _fromText.getText().toString();
        final String to = _toText.getText().toString();
        final String cost = _costText.getText().toString();
        final String padik = _padikText.getText().toString();
        final String code = _codeText.getText().toString();
        final String floor = _floorText.getText().toString();
        final String ko = _koText.getText().toString();
        final String num = _numText.getText().toString();
        final String rec = _recText.getText().toString();
        final String description = _descriptionText.getText().toString();
        final String payment = money;
        final String timeTake = _timeTakeText.getText().toString();
        final String timeBring = _timeBringText.getText().toString();
        final String weight = weight_;

        if (validate(description,from,to,num, ko)) {
            Order order = new Order(
                    user,
                    Order.FROM, from,
                    Order.TO, to,
                    Order.COST, cost,
                    Order.PAYMENT, payment,
                    Order.ENTRANCE, padik,
                    Order.CODE, code,
                    Order.FLOOR, floor,
                    Order.ROOM, ko,
                    Order.PHONE, num,
                    "recnum", rec,
                    Order.WEIGHT, weight,
                    Order.DESCRIPTION, description,
                    Order.TAKE_TIME, timeTake,
                    Order.BRING_TIME, timeBring
            );

            order.post(new InternetCallback<Boolean>() {
                @Override
                public void call(Boolean result) {
                    if (result) {
                        updateOrders();
                        updateFace();

                        Toast.makeText(
                                getActivity(),
                                "Заказ оформлен!",
                                Toast.LENGTH_LONG
                        ).show();

                        _fromText.setText("");
                        _toText.setText("");
                        _costText.setText("");
                        _padikText.setText("");
                        _codeText.setText("");
                        _floorText.setText("");
                        _koText.setText("");
                        _numText.setText("");
                        _recText.setText("");
                        _descriptionText.setText("");
                        _distanceView.setText("Расстояние: ");
                        _payView.setText("Оплата: ");
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "Произошла ошибка при оформлении заказа.",
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
            });
        }
    }

    // Проверка данных формы
    public boolean validate(String description, String from, String to, String num, String ko) {
        boolean valid = true;


        if (description.isEmpty()) {
            _descriptionText.setError("Назовите посылку, например, в соотсветствии с содержимым");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        if (from.isEmpty()) {
            _fromText.setError("Откуда взять посылку?");
            valid = false;
        } else {
            _fromText.setError(null);
        }


        if (to.isEmpty()) {
            _toText.setError("Куда доставить посылку?");
            valid = false;
        } else {
            _toText.setError(null);
        }

        if (num.isEmpty() || num.length() != 11) {
            _numText.setError("Некорректный номер телефона");
            valid = false;
        } else {
            _numText.setError(null);
        }

        return valid;
    }
}

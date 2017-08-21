package com.example.adrax.dely.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.Formula;
import com.example.adrax.dely.core.GoogleMapsHelper;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.core.Result;
import com.example.adrax.dely.core.ToastHelper;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
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

                        if (weight_.length()<3) {
                            weight_ += "000";
                        }
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

    // Призыв даилога
    public void buttonClicked () {
        final FragmentOrder me = this;

        GoogleMapsHelper.getInstance().calculateDistance(
                "Пермь," + _fromText.getText().toString(),
                "Пермь," + _toText.getText().toString(),
                new InternetCallback<Double>() {
                    @Override
                    public void call(Result<Double> result) {
                        if (result.isSuccessful()) {
                            Double distance = result.getData();
                            me.distance = distance.toString();
                            _distanceView.setText(distance + "м");
                            me.createOrderDialog();
                        } else {
                            _distanceView.setText(result.getMessage());
                            _payView.setText(result.getMessage());
                        }
                    }
                }
        );
    }

    private void createOrderDialog() {
        money = Formula.getDefault().calculate(
                Double.parseDouble(weight_),
                Double.parseDouble(distance)
        ).toString() + "руб.";

        _payView.setText(money);

        OrderDialog orderDialog = new OrderDialog();
        Bundle args = new Bundle();

        args.putString(Order.PAYMENT, money);
        args.putString(Order.TAKE_TIME, _timeTakeText.getText().toString());
        args.putString(Order.BRING_TIME, _timeBringText.getText().toString());
        args.putString(Order.FROM, _fromText.getText().toString());
        args.putString(Order.TO, _toText.getText().toString());
        args.putString(Order.PHONE, _numText.getText().toString());
        args.putString(Order.DESCRIPTION, _descriptionText.getText().toString());
        args.putString(Order.COST, _costText.getText().toString());
        args.putString(Order.WEIGHT, weight_);
        args.putString(Order.DAY,_dayText.getText().toString());
        orderDialog.setArguments(args);
        orderDialog.setTargetFragment(this, 0);
        orderDialog.show(getFragmentManager(), "OrderDialog");
        flagStart = false;
    }

    // Обратная связь с диалогом заказа
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            sendOrder();
    }

    // Отправка формы на сервер
    public void sendOrder() {
        final String from = _fromText.getText().toString();
        final String to = _toText.getText().toString();
        final String cost = _costText.getText().toString();
        final String entrance = _padikText.getText().toString();
        final String code = _codeText.getText().toString();
        final String floor = _floorText.getText().toString();
        final String room = _koText.getText().toString();
        final String phone = _numText.getText().toString();
        final String additionalPhone = _recText.getText().toString();
        final String description = _descriptionText.getText().toString();
        final String payment = money;
        final String takeTime = _timeTakeText.getText().toString();
        final String bringTime = _timeBringText.getText().toString();
        final String weight = weight_;

        if (validate(description, from, to, phone, room)) {
            Order order = new Order(
                    user,
                    Order.FROM, from,
                    Order.TO, to,
                    Order.COST, cost,
                    Order.PAYMENT, payment,
                    Order.ENTRANCE, entrance,
                    Order.CODE, code,
                    Order.FLOOR, floor,
                    Order.ROOM, room,
                    Order.PHONE, phone,
                    "recnum", additionalPhone,
                    Order.WEIGHT, weight,
                    Order.DESCRIPTION, description,
                    Order.TAKE_TIME, takeTime,
                    Order.BRING_TIME, bringTime
            );

            order.post(new InternetCallback<String>() {
                @Override
                public void call(Result<String> result) {
                    if (result.isSuccessful()) {
                        updateOrders();
                        updateFace();

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
                    }

                    ToastHelper.createToast(getActivity(), result.getMessage());
                }
            });
        }
    }

    // Проверка данных формы
    public boolean validate(
            @NonNull String description,
            @NonNull String from,
            @NonNull String to, String num,
            @NonNull String ko
    ) {
        boolean valid = true;

        if (description.isEmpty()) {
            _descriptionText.setError("Назовите посылку, например, в соответствии с содержимым");
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

package com.example.adrax.dely.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.delivery.DeliveryFormula;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.updateOrders;
import static com.example.adrax.dely.MActivity.updateFace;
import static com.example.adrax.dely.MActivity.waitingForCourier;


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
    EditText _distanceText;
    TextView _payView;
    EditText _dayText;
    EditText _timeTakeText;
    EditText _timeBringText;
    Calendar dayCalendar = Calendar.getInstance();
    Calendar takeCalendar = Calendar.getInstance();
    Calendar bringCalendar = Calendar.getInstance();
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
        _recText = (EditText)root.findViewById(R.id.input_recnum);
        _descriptionText = (EditText)root.findViewById(R.id.input_description);
        _payView = (TextView)root.findViewById(R.id.view_pay);
        _distanceText = (EditText)root.findViewById(R.id.input_distance);

        _distanceText.setText("5");
        _dayText = (EditText)root.findViewById(R.id.input_day);
        _timeTakeText = (EditText)root.findViewById(R.id.input_time_take);
        _timeBringText = (EditText)root.findViewById(R.id.input_time_bring);
        // Pickers
        // setup
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
        // End pickers

        final Button button =
                (Button) root.findViewById(R.id.btn_order);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonClicked();
            }
        });

        _fromText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!_fromText.getText().toString().equals("") && !_toText.getText().toString().equals("")){
                        DeliveryFormula formula = DeliveryFormula.getInstance();
                        if (_distanceText.getText().toString().equals("")) _distanceText.setText("0");
                        String message = "Стоимость заказа: "+formula.calculate(Integer.parseInt(_distanceText.getText().toString())).toString();
                        _payView.setText(message);
                    }
                }
            }
        });

        _toText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!_fromText.getText().toString().equals("") && !_toText.getText().toString().equals("")){
                        DeliveryFormula formula = DeliveryFormula.getInstance();
                        if (_distanceText.getText().toString().equals("")) _distanceText.setText("0");
                        String message = "Стоимость заказа: "+formula.calculate(Integer.parseInt(_distanceText.getText().toString())).toString();
                        _payView.setText(message);
                    }

                }
            }
        });


        _distanceText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                        DeliveryFormula formula = DeliveryFormula.getInstance();
                        if (_distanceText.getText().toString().equals("")) _distanceText.setText("0");
                        String message = "Стоимость заказа: "+formula.calculate(Integer.parseInt(_distanceText.getText().toString())).toString();
                        _payView.setText(message);
                }
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

    // Отправка формы на сервер
    public void buttonClicked () {
        // Говнище
        DeliveryFormula formula = DeliveryFormula.getInstance();
        if (_distanceText.getText().toString().equals("")) _distanceText.setText("0");
        //--------

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
        final String payment = formula.calculate(Integer.parseInt(_distanceText.getText().toString())).toString();

        if (validate(description,from,to,num, ko)) {
            Order order = new Order(
                    user,
                    Order.FROM, from,
                    Order.TO, to,
                    Order.CODE, cost,
                    Order.PAYMENT, payment,
                    Order.ENTRANCE, padik,
                    Order.CODE, code,
                    Order.FLOOR, floor,
                    Order.ROOM, ko,
                    Order.PHONE, num,
                    "recnum", rec,
                    Order.DESCRIPTION, description,
                    Order.TAKE_TIME, "undefined",
                    Order.BRING_TIME, "undefined"
            );

            /* user.order(user.getLogin(),
                    from,
                    to,
                    cost,
                    payment,
                    padik,
                    code,
                    floor,
                    ko,
                    num,
                    rec,
                    "",
                    "",
                    description); */

            order.post(new InternetCallback<Boolean>() {
                @Override
                public void call(Boolean result) {
                    if (result) {
                        updateOrders();
                        waitingForCourier(); //#UpdateTimerRun
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
                        _distanceText.setText("5");
                        _payView.setText("Стоимость заказа: ");
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

        if (ko.isEmpty()) {
            _numText.setError("Доставлять в квартиру или в офис?");
            valid = false;
        } else {
            _numText.setError(null);
        }

        return valid;
    }
}

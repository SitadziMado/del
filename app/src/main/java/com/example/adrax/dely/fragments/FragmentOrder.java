package com.example.adrax.dely.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adrax.dely.R;
import com.example.adrax.dely.core.InternetCallback;
import com.example.adrax.dely.core.Order;
import com.example.adrax.dely.delivery.DeliveryFormula;

import static com.example.adrax.dely.LoginActivity.user;
import static com.example.adrax.dely.MActivity.orders_update;
import static com.example.adrax.dely.MActivity.update_face;
import static com.example.adrax.dely.MActivity.WaitingForCourier;


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
                    "From", from,
                    "To", to,
                    "Cost", cost,
                    "Payment", payment,
                    "Padik", padik,
                    "Code", code,
                    "Floor", floor,
                    "Ko", ko,
                    "Nom", num,
                    "Recnum", rec,
                    "Dayoff", description
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

            user.order(order, new InternetCallback<Boolean>() {
                @Override
                public void call(Boolean result) {
                    if (result) {
                        orders_update();
                        WaitingForCourier(); //#UpdateTimerRun
                        update_face();

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

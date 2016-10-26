package com.jemsam.digitalmind.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jemsam.digitalmind.service.LoginAPiInterface;
import com.jemsam.digitalmind.model.LoginResponse;
import com.jemsam.digitalmind.R;
import com.jemsam.digitalmind.service.ServiceGenerator;
import com.jemsam.digitalmind.model.User;
import com.jemsam.digitalmind.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jemsam.digitalmind.utils.Utils.getPrefLogin;
import static com.jemsam.digitalmind.utils.Utils.getPrefPassword;
import static com.jemsam.digitalmind.utils.Utils.setPrefLogin;
import static com.jemsam.digitalmind.utils.Utils.setPrefPassword;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener {

    Button landingButtonSend;

    private TextInputLayout loginLayout;
    private Button landingButtonRegister;
    private TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = (TextInputLayout) findViewById(R.id.loginLayout);
        if (getPrefLogin(this) != null){
            loginLayout.getEditText().setText(getPrefLogin(this));
        }

        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        if (getPrefLogin(this) != null){
            passwordLayout.getEditText().setText(getPrefLogin(this));
        }


        landingButtonSend = (Button) this.findViewById(R.id.landingButtonConnect);
        landingButtonSend.setOnClickListener(this);

        landingButtonRegister = (Button) this.findViewById(R.id.landingButtonRegister);
        landingButtonRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String loginInput = loginLayout.getEditText().getText().toString();
        String passwordInput = passwordLayout.getEditText().getText().toString();

        if (loginInput.isEmpty()){
            loginLayout.setErrorEnabled(true);
            loginLayout.setError(getString(R.string.error_empty));
            return;
        } else {
            setPrefLogin(this, loginInput);
            loginLayout.setError(null);
        }

        if (passwordInput.isEmpty()){
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError(getString(R.string.error_empty));
            return;
        } else {
            setPrefPassword(this, passwordInput);
            passwordLayout.setError(null);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Utils.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginAPiInterface apiService = retrofit.create(LoginAPiInterface.class);

        switch (view.getId()){
            case R.id.landingButtonConnect:
                LoginAPiInterface loginService =
                        ServiceGenerator.createService(LoginAPiInterface.class, getPrefLogin(this), getPrefPassword(this));
                Call<LoginResponse> call = loginService.connectUser();
                call.enqueue(new Callback<LoginResponse >() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }
                });
                break;
            case R.id.landingButtonRegister:
                Call<LoginResponse> call2 = apiService.createUser(new User(getPrefLogin(this), getPrefPassword(this)));
                call2.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Register done", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "login or/and password already exist, or fields not properly set", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }


}

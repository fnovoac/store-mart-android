package com.shop_mart.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.asyncokhttp.AsyncOkHttp;
import com.liuguangqiang.asyncokhttp.BaseResponseHandler;
import com.shop_mart.R;
import com.shop_mart.constants.Constant;
import com.shop_mart.model.SignUpModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.usernameEdt)
    EditText username;

    @BindView(R.id.passwordEdt)
    EditText password;

    @BindView(R.id.loginBtn)
    Button loginBtn;

    @BindView(R.id.signUpTxt)
    TextView signUpText;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginBtn)
    public void doLogin(){
        if(isEmpty(username) || isEmpty(password)){
            showToast("username or password field cannot be empty");
            return;
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    @OnClick(R.id.signUpTxt)
    public void goToSignUp(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public boolean isEmpty(EditText editText) {
        if (editText.getText() == null || editText.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void showToast(String message) {
        if (message != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


    private void makeRequests(SignUpModel signUpModel) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", signUpModel.getUsername());
            jsonObject.put("email", signUpModel.getPassword());
            jsonObject.put("email", signUpModel.getPassword());
            jsonObject.put("email", signUpModel.getPassword());

            AsyncOkHttp okHttp = AsyncOkHttp.getInstance();
            okHttp.addHeader(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_TYPE);
            okHttp.post(Constant.LOG_URL, jsonObject.toString(), new BaseResponseHandler() {
                @Override
                public void onSuccess(int code, String responseString) {
                    progressDialog.dismiss();
                    try {
                        if (responseString != null) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.optBoolean("success")) {
                                JSONObject jsonData = jsonResponse.getJSONObject("data");
                                String username = jsonData.getString("username");
                                String email = jsonData.getString("email");

                                Toast.makeText(getApplicationContext(), "email: " + email + " " +
                                                "username:" + username,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int code, String responseString) {
                    progressDialog.dismiss();
                    try {
                        JSONObject response = new JSONObject(responseString);
                        if (!response.optBoolean("status")) {
                            String message = response.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

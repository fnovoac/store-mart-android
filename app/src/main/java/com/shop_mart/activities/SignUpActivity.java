package com.shop_mart.activities;

import android.app.Dialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.usernameEdt)
    EditText username;

    @BindView(R.id.emailEdt)
    EditText email;

    @BindView(R.id.passwordEdt)
    EditText password;

    @BindView(R.id.confirm_passwordEdt)
    EditText confirmPassword;

    @BindView(R.id.signUpBtn)
    Button signUpBtn;

    @BindView(R.id.signInTxt)
    TextView signInText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.signUpBtn)
    public void signUp(){
        if(isEmpty(username) || isEmpty(password) || isEmpty(email) || isEmpty(confirmPassword) ){
            showToast("field cannot be empty");
            return;
        }
        if (!confirmPassword.getText().toString().equals(password.getText().toString())){
            showToast("password does not match");
            return;
        }

        Map<String,String> requestMap = new HashMap<>();
        requestMap.put("username",username.getText().toString());
        requestMap.put("email", email.getText().toString());
        requestMap.put("password", password.getText().toString());
        requestMap.put("repeat_password", confirmPassword.getText().toString());

        sendRequest(requestMap);

    }

    @OnClick(R.id.signInTxt)
    public void goToLogin(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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


    private void sendRequest(Map<String, String> request) {
        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.loading);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", request.get("username"));
            jsonObject.put("email", request.get("email"));
            jsonObject.put("password", request.get("password"));
            jsonObject.put("repeat_password", request.get("repeat_password"));

            AsyncOkHttp okHttp = AsyncOkHttp.getInstance();
            okHttp.addHeader(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_TYPE);
            okHttp.post(Constant.SIGN_UP_URL, jsonObject.toString(), new BaseResponseHandler() {
                @Override
                public void onSuccess(int code, String responseString) {
                    alertDialog.dismiss();
                    try {
                        if (responseString != null) {
                            JSONObject jsonResponse = new JSONObject(responseString);
                            if (jsonResponse.optBoolean("status")) {
                                JSONObject jsonData = jsonResponse.getJSONObject("data");
                                String username = jsonData.getString("username");
                                String email = jsonData.getString("email");

                                Toast.makeText(getApplicationContext(), "email: " + email + " " +
                                        "username:" + username,Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }else if(!jsonResponse.optBoolean("status")){
                                if(jsonResponse.getString("message") != null){
                                    showToast(jsonResponse.getString("message"));
                                }
                                showToast("Email already exist");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int code, String responseString) {
                    alertDialog.dismiss();
                    try {
                        JSONObject response = new JSONObject(responseString);
                        if (!response.optBoolean("status")) {
                            String message = response.getString("message");
                            showToast(message);
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

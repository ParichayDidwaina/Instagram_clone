/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

  EditText username;
  EditText password;
  Button b;
  TextView change;

  public void userlist()
  {
    Intent i = new Intent(this,Main2Activity.class);
    startActivity(i);
  }

  public void hide(View view)
  {
    InputMethodManager i = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    i.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

  }

  public void changer(View view) {

        if(b.getText().equals("Login"))
      {
        b.setText("Sign up");
        change.setText(",or Login");
      }
      else
      {
        b.setText("Login");
        change.setText(",or Sign up");
      }

  }

  public void login_signup(View view)
  {
    if(b.getText().equals("Login")) {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
        Toast.makeText(this, "Username and Password Required", Toast.LENGTH_SHORT).show();

        } else {

          ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if(e!=null)
              {
                Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
              }
              else
              {
                Toast.makeText(MainActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                userlist();
              }
            }
          });
      }
    }
    else
    {
      if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
        Toast.makeText(this, "Username and Password Required", Toast.LENGTH_SHORT).show();

      }
      else {
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if(e==null)
            {
              Toast.makeText(MainActivity.this, "Account Made Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");

    username = (EditText) findViewById(R.id.editText);
    password = (EditText) findViewById(R.id.editText2);
    password.setOnKeyListener(this);
    b = (Button) findViewById(R.id.button2);
    change = (TextView) findViewById(R.id.text1);

    if(ParseUser.getCurrentUser()!=null)
    {
      userlist();
    }
    ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN)
    {
      login_signup(view);
    }
    return false;
  }
}
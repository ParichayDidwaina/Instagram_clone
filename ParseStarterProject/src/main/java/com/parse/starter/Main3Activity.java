package com.parse.starter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Main3Activity extends AppCompatActivity {
    LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

         l= (LinearLayout) findViewById(R.id.layout);

        String username = getIntent().getStringExtra("username");
        setTitle(username+"'s Photos");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0)
                {
                    for(ParseObject obj:objects)
                    {
                        ParseFile file =(ParseFile) obj.get("images");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null)
                                {
                                    Bitmap b = BitmapFactory.decodeByteArray(data,0,data.length);

                                    ImageView img = new ImageView(getApplicationContext());

                                    img.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT));

                                    img.setPadding(0,10,0,10);

                                    img.setImageBitmap(b);
                                    l.addView(img);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}

package com.parse.starter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity{

    int flag = 0;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
                {
                    getphoto();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.share)
        {

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else {
                flag=1;
                getphoto();
            }
            return true;
        }
        else if(item.getItemId()==R.id.logout)
        {
            ParseUser.logOut();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

            return true;
        }

        return false;
    }

    public void getphoto()
    {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri id = data.getData();
            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                try {
                    Bitmap b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), id);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    ParseFile file = new ParseFile("Image.png", bytes);

                    ParseObject object = new ParseObject("Image");

                    object.put("images", file);

                    object.put("username", ParseUser.getCurrentUser().getUsername());

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(Main2Activity.this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("User Feed");

        final ListView l = (ListView) findViewById(R.id.list);
        final ArrayList<String> a = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,a);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size()>0)
                {
                    for(ParseUser user:objects)
                    {
                        a.add(user.getUsername());
                        adapter.notifyDataSetChanged();
                    }
                    l.setAdapter(adapter);
                }
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Main3Activity.class);
                intent.putExtra("username",a.get(i));
                startActivity(intent);
            }
        });
    }


}

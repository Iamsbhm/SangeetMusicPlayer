package com.silicon.sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MusicList extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        listView = findViewById(R.id.ListViewDashBoard);

        /* Dexter Library used for seeking permission from user
        for reading the data from storage
         */
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        ArrayList<File> MySongs = getsong(Environment.getExternalStorageDirectory());
                        String [] songs =  new String[MySongs.size()];
                        for(int i =0; i<MySongs.size();i++)
                        {
                            /* removes .mp3 from the end of the song name*/
                            songs[i] = MySongs.get(i).getName().replace(".mp3"," ");

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MusicList.this, android.R.layout.simple_list_item_1,songs);
                        listView.setAdapter(adapter);


                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(MusicList.this, MainBoard.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                i.putExtra("mysong", MySongs); // sending Arraylist to next activity
                                i.putExtra("currentSong",currentSong); // sending the clicked song
                                i.putExtra("position",position); //sending the position to next activity
                                startActivity(i);
                            }
                        });




                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // if permission denied displays this message

                        Toast.makeText(MusicList.this, "Permission Denied", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        //  if permission is niether accepted nor denied then it sends request again when the app is opened
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }

    //get song function for reading the data. The data gets added on the arraylist.
    public ArrayList<File> getsong(File file){
        ArrayList  arrayList = new ArrayList<>();
        File[] songs = file.listFiles();

        if(songs!= null)
        {
            for(File myfile : songs)
            {
                if(!myfile.isHidden() && myfile.isDirectory())
                {
                    arrayList.addAll(getsong(myfile));

                }
                else
                {
                    if(myfile.getName().endsWith(".mp3")&& !myfile.getName().startsWith("."))
                    {
                        arrayList.add(myfile);
                    }
                }

            }
        }


        return arrayList;
    }




}
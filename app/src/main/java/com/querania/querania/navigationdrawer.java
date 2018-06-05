package com.querania.querania;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
public class CustomDialog extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_dialog,container,false);
        getDialog().setTitle("Sample");
        Button mDoneBtn = (Button) mView.findViewById(R.id.done_convert);
        mDoneBtn.setOnClickListener(DoneAction);
        return mView;
    }

    View.OnClickListener DoneAction = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG).show();
        }
    };

}
*/
public class navigationdrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    int s,sessionid;
    String data,s1;
    JSONObject jsonObject;
    ArrayList<carddata> cd;
    RecyclerView rv;
    RecyclerView.LayoutManager lm;
    RVAdapter rvd;
    private static final int REQUEST_STORAGE = 0;
    private static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    private static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
    private ImageView selectedImage;
    StringBuilder myimage,newquestion;
    MaterialDialog m=null;
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    File imagefile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigationdrawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        cd=new ArrayList<>();
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(navigationdrawer.this);
        sessionid=sharedPref.getInt("sessionuserid", -1);
        s=sessionid;
        data="{\"sessionid\":\""+sessionid+"\"}";
        Log.v("logv",data);
        rv=(RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(navigationdrawer.this);
        rv.setLayoutManager(lm);
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://www.palzone.ml/services/loadquestions.php")
                        .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        navigationdrawer.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(navigationdrawer.this, "error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        s1= response.body().string();

                        navigationdrawer.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(home_page.this,s1, Toast.LENGTH_LONG).show();

                                String s2=s1.substring(s1.indexOf('[')+1,s1.lastIndexOf(']'));
                                String s3[]=s2.split("[}][,]");

                                for(String s4:s3)
                                {
                                    Log.v("response",s4+'}');

                                }
                                for (int i=0;i<s3.length;i++)
                                {
                                    if(i!=s3.length-1)
                                    {
                                        s3[i]=s3[i]+'}';
                                    }
                                    try {
                                        jsonObject =new JSONObject(s3[i]);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        String userpic2=jsonObject.getString("userpic2");
                                        int id=jsonObject.getInt("id");
                                        int quserid=jsonObject.getInt("quserid");
                                        String ques=jsonObject.getString("ques");
                                        String questionaskname=jsonObject.getString("questionaskname");
                                        int nooflike=jsonObject.getInt("nooflike");
                                        int noofunlike=jsonObject.getInt("noofunlike");
                                        int noofanswers=jsonObject.getInt("noofanswers");
                                        String statuslike=jsonObject.getString("statuslike");
                                        String statusunlike=jsonObject.getString("statusunlike");
                                        int sessionuserid=jsonObject.getInt("sessionuserid");
                                        int follow=jsonObject.getInt("follow");
                                        int editqbtn=jsonObject.getInt("editqbtn");
                                        int likeqbtn=jsonObject.getInt("likeqbtn");
                                        int unlikeqbtn=jsonObject.getInt("unlikeqbtn");

                                        cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }



                                rvd=new RVAdapter(cd,navigationdrawer.this);
                                rv.setAdapter(rvd);




                            }
                        });
                    }
                });

            }
        });
        th.start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                //FragmentManager fm = getSupportFragmentManager();
                //CustomDialog custom = new CustomDialog();
                //custom.show(fm,"");
                    final Thread th2=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar c = Calendar.getInstance();
                            //System.out.println("Current time => " + c.getTime());

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c.getTime());

                            data="{\"sessionuserid\":\""+sessionid+"\",\"newquestion\":\"" + newquestion + "\",\"postdate\":\"" + formattedDate + "\"}";

                            Log.v("The requested data is",data);
                            OkHttpClient client=new OkHttpClient();
                            Request request=new Request.Builder()
                                    .url("https://www.palzone.ml/services/postquestion1.php")
                                    .post(RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),data))
                                    .build();

                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    navigationdrawer.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(navigationdrawer.this, "error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    s1= response.body().string();

                                    navigationdrawer.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.makeText(home_page.this,s1, Toast.LENGTH_LONG).show();
                                            Log.v("s1",s1);
                                            String s2=s1.substring(s1.indexOf('[')+1,s1.lastIndexOf(']'));
                                            String s3[]=s2.split("[}][,]");

                                            for(String s4:s3)
                                            {
                                                Log.v("response",s4+'}');

                                            }
                                            for (int i=0;i<s3.length;i++)
                                            {
                                                if(i!=s3.length-1)
                                                {
                                                    s3[i]=s3[i]+'}';
                                                }
                                                try {
                                                    jsonObject =new JSONObject(s3[i]);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    String userpic2=jsonObject.getString("userpic");
                                                    int id=jsonObject.getInt("userid");
                                                    int quserid=jsonObject.getInt("qid");
                                                    String ques=jsonObject.getString("question");
                                                    String questionaskname=jsonObject.getString("username");
                                                    int nooflike=0;
                                                    int noofunlike=0;
                                                    int noofanswers=0;
                                                    String statuslike="2";
                                                    String statusunlike="2";
                                                    int sessionuserid=jsonObject.getInt("userid");
                                                    int follow=0;
                                                    int editqbtn=0;
                                                    int likeqbtn=0;
                                                    int unlikeqbtn=0;

                                                    //cd.add(i,new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));
                                                rvd.addelement(new carddata(userpic2,id,quserid,ques,questionaskname,nooflike,noofunlike,noofanswers,statuslike,statusunlike,sessionuserid,follow,editqbtn,likeqbtn,unlikeqbtn));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }



                                            rvd=new RVAdapter(cd,navigationdrawer.this);
                                            rv.setAdapter(rvd);




                                        }
                                    });
                                }
                            });



                        }
                    });

                boolean wrapInScrollView = true;
                m= new MaterialDialog.Builder(navigationdrawer.this)
                        .title("Ask Your Question")
                        .customView(R.layout.comment, wrapInScrollView)
                        .positiveText("Post").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {



                                EditText questionpost=(EditText)m.getCustomView().findViewById(R.id.questionpost);


                                //Log.v("The file path is ",selectedImageUri.getPath());
                                try {
                                    Drawable d = ((ImageView) m.findViewById(R.id.image_picker_selected)).getDrawable();
                                    Bitmap bm = ((GlideBitmapDrawable) d.getCurrent()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                                    byte[] b = baos.toByteArray();
                                    myimage = new StringBuilder(Base64.encodeToString(b, Base64.DEFAULT));
                                    //String myimage1 = "sjkfsdfhsd";
                                    newquestion = new StringBuilder("<p>" + questionpost.getText().toString() + "</p>" + "<center><img style='max-width:80%;max-height:80%;' src='data:image/jpeg;base64," + (myimage.toString()).replace("\n","") + "'" + "></center>");
                                }
                                catch(Exception e){

                                    newquestion = new StringBuilder("<p>" + questionpost.getText().toString() + "</p>");
                                }
                                Log.v("The text is ",newquestion.toString());
                                Log.v("last 5 ",(newquestion.toString()).substring((newquestion.toString()).length()-5));

                            th2.start();
                            }

                        })
                        .show();
                View view2 = m.getCustomView();
                bottomSheetLayout = (BottomSheetLayout) view2.findViewById(R.id.bottomsheet);
                bottomSheetLayout.setPeekOnDismiss(true);
                selectedImage = (ImageView) view2.findViewById(R.id.image_picker_selected);

                selectedImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkNeedsPermission()) {
                            requestStoragePermission();
                        } else {
                            showSheetView();
                        }
                    }
                });



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigationdrawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            boolean wrapInScrollView = true;
            final Thread th2=new Thread(new Runnable() {
                @Override
                public void run() {

                    File sourceFile = imagefile;


                  //  Log.d(TAG, "File...::::" + sourceFile + " : " + sourceFile.exists());

                    final MediaType MEDIA_TYPE = (imagefile.getPath().toString()).endsWith("png") ?
                            MediaType.parse("image/png") : MediaType.parse("image/jpeg");


                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file", sourceFile.toString(), RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                            .addFormDataPart("sessionuserid", String.valueOf(sessionid))
                            .build();

                    Request request = new Request.Builder()
                            .url("https://www.palzone.ml/services/pic_upload.php")
                            .post(requestBody)
                            .build();

                    Log.v("aa",imagefile.toString());
                    OkHttpClient client=new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, final IOException e) {
                                navigationdrawer.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(navigationdrawer.this,e.getStackTrace().toString(),Toast.LENGTH_LONG).show();
                                    }
                                });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                                        Log.v("st",response.body().string());
                                        Log.v("st","hello");
                        }
                    });

                }
            });
            m= new MaterialDialog.Builder(navigationdrawer.this)
                    .title("Upload Your Pic")
                    .customView(R.layout.comment, wrapInScrollView)
                    .positiveText("Post").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {





                            //Log.v("The file path is ",selectedImageUri.getPath());

                            th2.start();
                        }

                    })
                    .show();



            View view2 = m.getCustomView();
            EditText questionpost=(EditText)m.getCustomView().findViewById(R.id.questionpost);
            questionpost.setVisibility(View.GONE);
            bottomSheetLayout = (BottomSheetLayout) view2.findViewById(R.id.bottomsheet);
            bottomSheetLayout.setPeekOnDismiss(true);
            selectedImage = (ImageView) view2.findViewById(R.id.image_picker_selected);

            selectedImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkNeedsPermission()) {
                        requestStoragePermission();
                    } else {
                        showSheetView();
                    }
                }
            });



            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent myprofilepage=new Intent(navigationdrawer.this,myprofile.class);
            startActivity(myprofilepage);

        } else if (id == R.id.notification) {

            Intent notificationpage=new Intent(navigationdrawer.this,notification.class);
            startActivity(notificationpage);

        } else if (id == R.id.logout) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(navigationdrawer.this);
            SharedPreferences.Editor ed = sharedPrefs.edit();
            ed.remove("sessionuserid").commit();

            Intent loginpage = new Intent(navigationdrawer.this, login.class);
            startActivity(loginpage);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean checkNeedsPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(navigationdrawer.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            // Eh, prompt anyway
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSheetView();
            } else {
                // Permission denied
                Toast.makeText(this, "Sheet is useless without access to external storage :/", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Show an {@link ImagePickerSheetView}
     */
    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(this)
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(navigationdrawer.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isCameraTile()) {
                            dispatchTakePictureIntent();
                        } else if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);
                        } else if (selectedTile.isImageTile()) {
                            showSelectedImage(selectedTile.getImageUri());
                        } else {
                            genericError();
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    /**
     * For images captured from the camera, we need to create a File first to tell the camera
     * where to store the image.
     *
     * @return the File created for the image to be store under.
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    /**
     * This checks to see if there is a suitable activity to handle the `ACTION_PICK` intent
     * and returns it if found. {@link Intent#ACTION_PICK} is for picking an image from an external app.
     *
     * @return A prepared intent if found.
     */

    private Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    /**
     * This checks to see if there is a suitable activity to handle the {@link MediaStore#ACTION_IMAGE_CAPTURE}
     * intent and returns it if found. {@link MediaStore#ACTION_IMAGE_CAPTURE} is for letting another app take
     * a picture from the camera and store it in a file that we specify.
     *
     * @return A prepared intent if found.
     */

    private Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    /**
     * This utility function combines the camera intent creation and image file creation, and
     * ultimately fires the intent.
     *
     * @see {@link #createCameraIntent()}
     * @see {@link #createImageFile()}
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = createCameraIntent();
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent != null) {
            // Create the File where the photo should go
            try {
                File imageFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                // Error occurred while creating the File
                genericError("Could not create imageFile for camera");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
                selectedImage = data.getData();
                if (selectedImage == null) {
                    genericError();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                selectedImage = cameraImageUri;
            }

            if (selectedImage != null) {
                showSelectedImage(selectedImage);
            } else {
                genericError();
            }
        }
    }

    private void showSelectedImage(Uri selectedImageUri) {
        selectedImage.setImageDrawable(null);
        //Bitmap bm= BitmapFactory.decodeFile(selectedImageUri);
        imagefile=new File(selectedImageUri.getPath());
        Glide.with(this)
                .load(selectedImageUri)
                .crossFade()
                .fitCenter()
                .into(selectedImage);



    }

    private void genericError() {
        genericError(null);
    }

    private void genericError(String message) {
        Toast.makeText(this, message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }
}

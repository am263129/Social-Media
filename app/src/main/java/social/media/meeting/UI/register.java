package social.media.meeting.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import social.media.meeting.MainActivity;
import social.media.meeting.R;

public class register extends AppCompatActivity {
    EditText username_edt, email_edt, password_edt, address_edt, phone_edt, area_edt;
    ImageView profile_pic;
    TextView birthday_edt;
    Button register_btn;
    RadioGroup genderrb;
    RadioGroup memberrb;
    RadioButton male, female;
    File upload_file = null;
    public String TAG = "Register";
    public static final int PICK_IMAGE = 1;
    public Bitmap bitmap = null;
    public Uri filePath ;

    FirebaseAuth mAuth;

    private String userEmail;
    private String userPass;

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private static final int PICK_IMAGE_REQUEST = 234;
    private int year, month, day;
    private DatePickerDialog picker;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        username_edt = (EditText) findViewById(R.id.username_edt);
        email_edt = (EditText) findViewById(R.id.email_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        address_edt = (EditText) findViewById(R.id.address_edt);
        birthday_edt = (TextView) findViewById(R.id.birthday_edt);
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        area_edt = (EditText) findViewById(R.id.area_edt);

        genderrb = (RadioGroup) findViewById(R.id.genderrb);
        memberrb = (RadioGroup) findViewById(R.id.memberrb);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        register_btn = (Button) findViewById(R.id.register_btn);
        male = (RadioButton) findViewById(R.id.malerb);
        female = (RadioButton) findViewById(R.id.femalerb);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });



        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkemail() & check_username() & check_password()) {

                    int selectedId = genderrb.getCheckedRadioButtonId();
                    int selectedmemberid = memberrb.getCheckedRadioButtonId();
                    RadioButton radiogender = (RadioButton) findViewById(selectedId);
                    RadioButton radiomember = (RadioButton) findViewById(selectedmemberid);
                    String selected_gender = radiogender.getText().toString();
                    String membership = radiomember.getText().toString();
                    String username = username_edt.getText().toString();
                    String email = email_edt.getText().toString();
                    String password = password_edt.getText().toString();


                    RegisterAPI(username, email, password, selected_gender, membership, upload_file);

                }


            }
        });

        birthday_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                picker = new DatePickerDialog(register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthday_edt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                year = year;
                                month = monthOfYear;
                                day = dayOfMonth;
                            }
                        }, year, month , day);

                picker.show();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


    }
    @Override
    protected void onStart() {

        super.onStart();
    }

    public boolean checkemail() {

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(
                email_edt.getText().toString()).matches()) {

            return true;
        } else {
            email_edt.setError("Email Invalid");
            return false;
        }
    }


    public boolean check_username() {

        if (!TextUtils.isEmpty(username_edt.getText()) & username_edt.getText().length() > 2)
            return true;
        else {
            username_edt.setError("User Name atleast 3 characters needed");
            return false;
        }
    }

    public boolean check_password() {

        if (!TextUtils.isEmpty(password_edt.getText()) & password_edt.getText().length() > 2)
            return true;
        else {
            password_edt.setError("Atleast three characters required");
            return false;
        }
    }

    public void RegisterAPI(String username, final String email, final String password, String gender,String membership, File file1) {
        userEmail = email_edt.getText().toString();
        userPass = password;
        FirebaseApp.initializeApp(this);
        String id = "Member/" + username;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(id+"/Name");
        myRef.setValue(username);
        myRef = database.getReference(id+"/Email");
        myRef.setValue(email);
        myRef = database.getReference(id+"/Password");
        myRef.setValue(password);
        myRef = database.getReference(id+"/Address");
        myRef.setValue(address_edt.getText().toString());
        myRef = database.getReference(id+"/Birthday");
        myRef.setValue(birthday_edt.getText().toString());
        myRef = database.getReference(id+"/Area");
        myRef.setValue(area_edt.getText().toString());
        myRef = database.getReference(id+"/Phone");
        myRef.setValue(phone_edt.getText().toString());
        myRef = database.getReference(id+"/Gender");
        myRef.setValue(gender);
        myRef = database.getReference(id+"/Membership");
        myRef.setValue(membership);
        myRef = database.getReference(id+"/Private");
        myRef.setValue("False");
        if (bitmap!=null) {
            myRef = database.getReference(id + "/Photo");
            myRef.setValue(getBase64String(bitmap));
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is:" + value);
                singUp();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG,"Failed to rad value ", databaseError.toException());
            }
        });
    }
    private void singUp(){
        mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.e(TAG,"Successful");
                    Intent intent = new Intent(register.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(register.this,"New user Added Successfully!", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(register.this,"New user Adding Failed!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                profile_pic.setImageBitmap(bitmap);
                upload_file = new File(data.getData().getPath());
                filePath = data.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * encode
     * @param bitmap
     * @return String
     */
    private String getBase64String(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    /* decode
    private void decodeBase64AndSetImage(String completeImageData, ImageView imageView) {

        // Incase you're storing into aws or other places where we have extension stored in the starting.
        String imageDataBytes = completeImageData.substring(completeImageData.indexOf(",")+1);

        InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));

        Bitmap bitmap = BitmapFactory.decodeStream(stream);

        imageView.setImageBitmap(bitmap);
    }*/
}

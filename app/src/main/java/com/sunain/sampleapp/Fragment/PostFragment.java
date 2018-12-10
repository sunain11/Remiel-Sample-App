package com.sunain.sampleapp.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.sunain.sampleapp.Adapters.ProductTypeListAdapter;
import com.sunain.sampleapp.R;
import com.sunain.sampleapp.Utility.Utils;
import com.sunain.sampleapp.model.Post;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

import static android.app.Activity.RESULT_OK;


public class PostFragment extends Fragment {

    private FirebaseAuth mAuth;
    String TAG="PostFragment";
    public static String Storage_Path = "Remiel/All_Post_Uploads/";
    public static String Database_Path = "Remiel/All_Post_Uploads_Database";
    public String image_name;
    Button ChooseButton, UploadButton;
    EditText Imagetitle ,Imagecontent;
    ImageView SelectImage;
    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    int Image_Request_Code = 7;
    Context applicationContext;
    ProgressDialog progressDialog ;
    int GALLERY = 1,CAMERA=2;
    private SearchableSpinner mSearchableSpinner;
    private ProductTypeListAdapter mSimpleListAdapter;
    private final ArrayList<String> mStrings = new ArrayList<>();
    public PostFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_post, container, false);
        mAuth = FirebaseAuth.getInstance();
//        Toasty.info(getApplicationContext(),"Hello",Toast.LENGTH_SHORT).show();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path+"/"+Utils.encodeEmail(mAuth.getCurrentUser().getEmail()));
        ChooseButton = rootView.findViewById(R.id.ButtonChooseImage);
        UploadButton = rootView.findViewById(R.id.ButtonUploadImage);
        // Assign ID's to EditText.
        Imagetitle = rootView.findViewById(R.id.ImagetitleEditText);
        Imagecontent= rootView.findViewById(R.id.ImagecontentEditText);
        SelectImage = rootView.findViewById(R.id.ShowImageView);
        progressDialog = new ProgressDialog(getActivity());
        initListValues();
        mSimpleListAdapter = new ProductTypeListAdapter(getApplicationContext(), mStrings);
        mSearchableSpinner = rootView.findViewById(R.id.SearchableSpinner);
        mSearchableSpinner.setAdapter(mSimpleListAdapter);
        mSearchableSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
        mSearchableSpinner.setStatusListener(new IStatusListener() {
            @Override
            public void spinnerIsOpening() {
            }

            @Override
            public void spinnerIsClosing() {

            }
        });

        ChooseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {





                String[] colors = {"Camera", "gallery"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Pick an option");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if(which==0)
                        {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA);
                        }
                        if(which==1)
                        {
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY);
                        }
                    }
                });
                builder.show();


            }
        });

        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toasty.info(getApplicationContext(),mSearchableSpinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                UploadImageFileToFirebaseStorage();

            }
        });
        return rootView;
    }
    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            Toast.makeText(getApplicationContext(), "Item on position " + position + " : " + mSimpleListAdapter.getItem(position) + " Selected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };
    private void initListValues()
    {
        mStrings.add("Brigida Kurz");
        mStrings.add("Tracy Mckim");
        mStrings.add("Iesha Davids");
        mStrings.add("Ozella Provenza");
        mStrings.add("Florentina Carriere");
        mStrings.add("Geri Eiler");
        mStrings.add("Tammara Belgrave");
        mStrings.add("Ashton Ridinger");
        mStrings.add("Jodee Dawkins");
        mStrings.add("Florine Cruzan");
        mStrings.add("Latia Stead");
        mStrings.add("Kai Urbain");
        mStrings.add("Liza Chi");
        mStrings.add("Clayton Laprade");
        mStrings.add("Wilfredo Mooney");
        mStrings.add("Roseline Cain");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
//                Picasso.with(getApplicationContext()).load(FilePathUri).centerCrop().into(SelectImage);
                Picasso.with(getApplicationContext()).load(FilePathUri).fit().into(SelectImage);
//                Uri selectedImage = Uri.parse(getRealPathFromURI(data.getData()));
//                Bitmap myBitmap = BitmapFactory.decodeFile(selectedImage.toString());
//                SelectImage.setImageBitmap(myBitmap);
                ChooseButton.setText("Image Selected");
            }
            catch (Exception e) {
                Log.e(TAG,e.toString());
                e.printStackTrace();
            }
        }
        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                Bitmap myBitmap = BitmapFactory.decodeFile(getRealPathFromURI(tempUri));
//                Picasso.with(getApplicationContext()).load(FilePathUri).into(SelectImage);
                SelectImage.setImageBitmap(myBitmap);
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), FilePathUri);
//                SelectImage.setImageBitmap(bitmap);
                ChooseButton.setText("Image Selected");
            }
            catch (Exception e) {
                Log.e(TAG,e.toString());
                e.printStackTrace();
            }
        }
        SelectImage.setVisibility(View.VISIBLE);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
    public void UploadImageFileToFirebaseStorage() {
        if (FilePathUri != null) {

            Utils utils = new Utils(getActivity());
            if (utils.isNetworkAvailable()){
                progressDialog.setTitle("Post is Uploading...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
            else
            {
//                int y=rand.nextInt(2);
//                Reactiondialog ld=new Reactiondialog(getActivity(), Reactor.rectionimg(y+1),"Oops! no internet connection!");
//                ld.show();
                progressDialog.dismiss();
            }
            image_name="#"+mAuth.getCurrentUser().getDisplayName()+ System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);
            final StorageReference storageReference2nd = storageReference.child(Storage_Path +image_name);
            final StorageReference filePath = storageReference.child(Storage_Path +image_name);
            filePath.putFile(FilePathUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downUri = task.getResult();
                        Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                        String TempImageName = Imagetitle.getText().toString().trim();
                        String TempImageTitle = Imagecontent.getText().toString().trim();
                        progressDialog.dismiss();
//                            Reactiondialog ld=new Reactiondialog(getActivity(), Reactor.rectionimg(5),"Post Uploaded Successfully ");
//                            ld.show();
//                            Postinfo imageUploadInfo = new Postinfo(TempImageName,TempImageTitle, taskSnapshot.getDownloadUrl().toString(),sp.getUserEmail(),sp.getuid(),image_name);
                        String ImageUploadId = databaseReference.push().getKey();
                        Post p=new Post(ImageUploadId,Utils.encodeEmail(mAuth.getCurrentUser().getEmail()),String.valueOf(System.currentTimeMillis()),downUri.toString(),TempImageName,TempImageTitle);
                        databaseReference.child(ImageUploadId).setValue(p)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toasty.success(getApplicationContext(),"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new ListFragment()).commit();
                                        getActivity().setTitle("Product List");
                                    }
                                });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
//            storageReference2nd.putFile(FilePathUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            String TempImageName = Imagetitle.getText().toString().trim();
//                            String TempImageTitle = Imagecontent.getText().toString().trim();
//                            progressDialog.dismiss();
//                            String ImageUploadId = databaseReference.push().getKey();
//                            Post p=new Post(ImageUploadId,mAuth.getCurrentUser().getDisplayName(),String.valueOf(System.currentTimeMillis()),storageReference2nd.getDownloadUrl().toString(),TempImageName,TempImageTitle);
//                            databaseReference.child(ImageUploadId+"_"+TempImageName).setValue(p);
////                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,new WritepostFragment()).commit();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            progressDialog.dismiss();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            progressDialog.setTitle("Post is Uploading...");
//
//                        }
//                    });
        }
        else {
//            Reactiondialog ld=new Reactiondialog(getActivity(), Reactor.rectionimg(3), "Please Select Image or Add Image Name");
//            ld.show();

        }
    }

    public Context getApplicationContext() {
        return getContext();
    }

}

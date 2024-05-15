package com.amitbartfeld.myneighborhood.operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_properties.LoginActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.properties_only_activities.NewUserPropertiesActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_properties.RegisterActivity;
import com.amitbartfeld.myneighborhood.constants.CloudNames;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.listeners.AmitDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.AmitDownloadsListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.AmitItemPropertiesDownloadListener;
import com.amitbartfeld.myneighborhood.listeners.AmitUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.AmitVerificationListener;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerStorage;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorVerificationListener;
import com.amitbartfeld.myneighborhood.listeners.ItemPropertiesInternetErrorDownloadListener;
import com.amitbartfeld.myneighborhood.neighborhood_encryption.Encryption;
import com.amitbartfeld.myneighborhood.properties.CodeProperties;
import com.amitbartfeld.myneighborhood.properties.UserItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudStorageProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CloudOperations {

    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static String getCloudBaseUrl(int neighborhoodNum) {
        return CloudNames.neighborhoodCollection+"/"+neighborhoodNum;
    }

    //send a specific property (user/market item/so on).
    public static void setItemPropertyDocumentInFirestore(ItemProperties properties, AmitUploadListenerFirestore listener) {
        //send to cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(properties.cloudUrl);
        documentReference.set(properties).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onUploadFinished(properties);
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    public static void removeItemPropertyFromFirestore(ItemProperties properties, AmitUploadListenerFirestore listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(properties.cloudUrl);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onUploadFinished(properties);
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    public static void logout(Context c) {
        auth.signOut();
        StorageOperations.deleteEverything(c);
    }

    //send a specific property that is not defined (not market item/user/so on).
    public static void setSpecificDocumentInCloudFirestore(String path, String fieldName, Object value, AmitUploadListenerFirestore listener) {
        //send to cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(path);
        HashMap<String, Object> map = new HashMap<>();
        map.put(fieldName, value);
        documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onUploadFinished(null);
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    public static void updatePhoneArray(String phoneNum, int neighborhoodNum, AmitUploadListenerFirestore listener) {
        //send to cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(getCloudBaseUrl(neighborhoodNum));
        documentReference.update(CloudPropertiesNames.phoneNumberList, FieldValue.arrayUnion(phoneNum)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onUploadFinished(null);
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    public static void getCodeMechanism(AmitDownloadListenerFirestore listener) {
        getSpecificDocumentFromCloudFirestore(new CodeProperties(null).cloudUrl, listener);
    }

    //get a specific document. For example, get specific user/specific market item/so on.
    public static void getItemPropertyDocumentFromFirestore(ItemProperties properties, AmitItemPropertiesDownloadListener listener) {
        //get from cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(properties.cloudUrl);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onDownloadFinished(new ItemProperties(task.getResult()));
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    //get a specific document. For example, get specific user/specific market item/so on.
    public static void getSpecificDocumentFromCloudFirestore(String path, AmitDownloadListenerFirestore listener) {
        //get from cloud
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.document(path);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onDownloadFinished(task.getResult());
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    //get all documents in a collection. For example, get all market items/event items/so on.
    public static void getPropertyCollectionFromCloudFirestore(ItemProperties properties, AmitDownloadsListenerFirestore listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionPath = properties.cloudUrl.substring(0, properties.cloudUrl.lastIndexOf("/"));
        CollectionReference collectionReference = db.collection(collectionPath);
        Field mapField = null;
        for (Field field : CloudProperties.class.getFields())
            if (field.getType().equals((Map.class))) {
                mapField = field;
                break;
            }
        //Firebase saves the property map inside the document and the fields inside of it. In order to access a field that is in the map we need to write "mapVariableName"."fieldName". So I had to get the map name using reflection.
        collectionReference.orderBy(mapField.getName()+"."+GeneralOperations.ScreenTypeGeneralOperations.getOrderByFromScreenType(properties.type)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (listener != null)
                        listener.onDownloadsFinished(task.getResult().getDocuments());
                } else if (listener != null)
                    listener.onError(task.getException().toString());
            }
        });
    }

    public static void getPhoneNumberNeighborhoodNum(String phoneNumber, AmitDownloadListenerFirestore listener) {
        // search if it exists in some neighborhood, and return neighborhood num. if it does not, return 0. if it is, return the neighborhood num.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query wantedFolder = db.collection(CloudNames.neighborhoodCollection).whereArrayContains(CloudPropertiesNames.phoneNumberList, phoneNumber);
        wantedFolder.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    if (snapshots.isEmpty()) {
                        if (listener != null)
                            listener.onDownloadFinished(null);
                    } else if (listener != null){
                        listener.onDownloadFinished(snapshots.getDocuments().get(0));
                    }
                } else if (listener != null){
                    listener.onError(task.getException().toString());
                }
            }
        });
    }

    public static void getLastNeighborhoodNum(AmitDownloadListenerFirestore listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query wantedFolder = db.collection(CloudNames.neighborhoodCollection).orderBy(CloudPropertiesNames.neighborhoodNum, Query.Direction.DESCENDING).limit(1);
        wantedFolder.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    if (snapshots.isEmpty()) {
                        if (listener != null)
                            listener.onDownloadFinished(null);
                    } else if (listener != null){
                        listener.onDownloadFinished(snapshots.getDocuments().get(0));
                    }
                } else if (listener != null){
                    listener.onError(task.getException().toString());
                }
            }
        });
    }

    public static void registerToNewNeighborhood(String phoneNumber, String fullName, RegisterActivity activity) {
        authenticatePhone(phoneNumber, activity, new AmitVerificationListener() {
            @Override
            public void onError(String message) {
                activity.showErrorMessage(MessagesTexts.sendingCodeToPhoneAuthError);
            }

            @Override
            public void onVerificationSucceeded() {
                getCodeMechanism(new InternetErrorDownloadListenerFirestore(activity) {
                    @Override
                    public void onDownloadFinished(DocumentSnapshot document) {
                        getLastNeighborhoodNum(new InternetErrorDownloadListenerFirestore(activity) {
                            @Override
                            public void onDownloadFinished(DocumentSnapshot document2) {
                                int neighborhoodNumTemp = 1;
                                if (document2 != null && document2.exists())
                                    neighborhoodNumTemp = Integer.parseInt(document2.getId()) + 1;
                                final int neighborhoodNum = neighborhoodNumTemp;
                                UserItemProperties user = new UserItemProperties(new GeneralOperations(phoneNumber, fullName, neighborhoodNum, true), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Register);
                                String neighborhoodCode = Encryption.encodeCode(neighborhoodNum, document.getString(CloudPropertiesNames.codeString));
                                StorageOperations.saveEverything(phoneNumber, fullName, user.generalOperations.isAdmin, neighborhoodCode, activity);
                                setSpecificDocumentInCloudFirestore(getCloudBaseUrl(neighborhoodNum), CloudPropertiesNames.neighborhoodNum, neighborhoodNum, new InternetErrorUploadListenerFirestore(activity) {
                                    @Override
                                    public void onUploadFinished(CloudProperties properties) {
                                        updatePhoneArray(phoneNumber, neighborhoodNum, new InternetErrorUploadListenerFirestore(activity) {
                                            @Override
                                            public void onUploadFinished(CloudProperties properties) {
                                                setItemPropertyDocumentInFirestore(user, new InternetErrorUploadListenerFirestore(activity) {
                                                    @Override
                                                    public void onUploadFinished(CloudProperties properties) {
                                                        if (user.generalOperations.isAdmin)
                                                            activity.onSignInSucceeded(user, neighborhoodCode);
                                                        else
                                                            activity.onSignInSucceeded(user, null);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });
    }

    public static void registerToExistingNeighborhood(String phoneNumber, String fullName, String neighborhoodCode, int neighborhoodNum, RegisterActivity activity) {

        authenticatePhone(phoneNumber, activity, new AmitVerificationListener() {
            @Override
            public void onError(String message) {
                activity.showErrorMessage(MessagesTexts.sendingCodeToPhoneAuthError);
            }

            @Override
            public void onVerificationSucceeded() {
                UserItemProperties user = new UserItemProperties(new GeneralOperations(phoneNumber, fullName, neighborhoodNum, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Register);
                StorageOperations.saveEverything(phoneNumber, fullName, user.generalOperations.isAdmin, neighborhoodCode, activity);
                updatePhoneArray(phoneNumber, neighborhoodNum, new InternetErrorUploadListenerFirestore(activity) {
                    @Override
                    public void onUploadFinished(CloudProperties properties) {
                        setItemPropertyDocumentInFirestore(user, new InternetErrorUploadListenerFirestore(activity) {
                            @Override
                            public void onUploadFinished(CloudProperties properties) {
                                activity.onSignInSucceeded(user, null);
                            }
                        });
                    }
                });
            }
        });
    }

    public static void authenticatePhone(String phoneNumber, NewUserPropertiesActivity activity, AmitVerificationListener listener) {
        auth.setLanguageCode("he");
        getPhoneNumber(activity, phoneNumber, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            AlertDialog dialog;

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (auth.getCurrentUser() != null) {
                            if (dialog != null)
                                dialog.dismiss();
                            if (listener != null)
                                listener.onVerificationSucceeded();
                        } else
                            activity.showErrorMessage(MessagesTexts.errorInEnteringCode);
                    }
                });
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                EditText editText = ((EditText)activity.getLayoutInflater().inflate(R.layout.code_edit_text, null));
                dialog = new AlertDialog.Builder(activity).setView(editText).setMessage(MessagesTexts.writeTheCode).setPositiveButton("תבדוק", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(s, editText.getText().toString().trim());
                        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    try {
                                        task.getResult();
                                        if (listener != null)
                                        listener.onVerificationSucceeded();
                                    } catch (Exception e) {
                                        activity.showErrorMessage(MessagesTexts.wrongCode);
                                    }
                                }
                                else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        if (listener != null)
                                        listener.onVerificationSucceeded();
                                    } else
                                        activity.showErrorMessage(MessagesTexts.errorInEnteringCode);
                                }
                            }
                        });
                    }
                }).create();
                dialog.show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (listener != null)
                listener.onError(e.toString());
            }
        });
    }

    public static void getPhoneNumber(NewUserPropertiesActivity activity, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks listener) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(CloudOperations.auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(listener).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public static void login(String phoneNumber, int neighborhoodNum, LoginActivity activity) {
        authenticatePhone(phoneNumber, activity, new InternetErrorVerificationListener(activity) {
            @Override
            public void onVerificationSucceeded() {
                UserItemProperties user = new UserItemProperties(new GeneralOperations(phoneNumber, null, neighborhoodNum, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Login);
                getItemPropertyDocumentFromFirestore(user, new ItemPropertiesInternetErrorDownloadListener(activity) {
                    @Override
                    public void onDownloadFinished(ItemProperties properties) {
                        String fullName = (String)properties.properties.get(CloudPropertiesNames.fullName);
                        boolean isAdmin = (boolean)properties.properties.get(CloudPropertiesNames.isAdmin);
                        user.generalOperations = new GeneralOperations(phoneNumber, fullName, neighborhoodNum, isAdmin);
                        getCodeMechanism(new InternetErrorDownloadListenerFirestore(activity) {
                            @Override
                            public void onDownloadFinished(DocumentSnapshot document) {
                                String neighborhoodCode = Encryption.encodeCode(neighborhoodNum, document.getString(CloudPropertiesNames.codeString));
                                StorageOperations.saveEverything(phoneNumber, fullName, isAdmin, neighborhoodCode, activity);
                                activity.onSignInSucceeded(user, null);
                            }
                        });
                    }
                });
            }
        });
    }

    public static void uploadToCloudStorage(CloudStorageProperties properties, InternetErrorUploadListenerStorage listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference(properties.cloudUrl);
        Bitmap bitmap = (Bitmap) properties.properties.get(CloudPropertiesNames.image);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        reference.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                if (listener != null)
                                    listener.onUploadFinished(task.getResult().toString());
                            } else if (listener != null)
                                listener.onError(task.getException().getMessage());
                        }
                    });
                }
                else if (listener != null)
                    listener.onError(task.getException().getMessage());
            }
        });
    }
}

package com.brasma.sistemajuntas;

import com.google.firebase.database.FirebaseDatabase;

public class persistencia extends android.app.Application {
    //esta clase hay que volverla principal
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

package com.pertamina.portal;

import android.app.Application;

import com.pertamina.portal.utils.RealmMigrations;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(3) // Must be bumped when the schema changes
                .migration(new RealmMigrations()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }
}

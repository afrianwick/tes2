package com.pertamina.portal.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            final RealmObjectSchema userSchema = schema.get("Task");
            userSchema.addField("processStep", String.class);
            oldVersion++;
        } else if (oldVersion == 2) {
            /**
             public String action;
             public String documentType;
             public String name;
             public int dateOfIssue;
             public String filename;
             public String docName;
             public String docIssuer;
             public String companyCode;
             public String uploadDate;
             public String description;
             */
            schema.create("MyDocumentData");

            final RealmObjectSchema userSchema = schema.get("MyDocumentData");

            userSchema.addField("action", String.class);
            userSchema.addField("documentType", String.class);
            userSchema.addField("name", String.class);
            userSchema.addField("dateOfIssue", int.class);
            userSchema.addField("filename", String.class);
            userSchema.addField("docName", String.class);
            userSchema.addField("docIssuer", String.class);
            userSchema.addField("companyCode", String.class);
            userSchema.addField("uploadDate", String.class);
            userSchema.addField("description", String.class);

            oldVersion++;
        }
    }
}

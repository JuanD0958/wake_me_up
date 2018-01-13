package co.anbora.wakemeup.data;

import android.content.Context;
import android.support.annotation.NonNull;

import co.anbora.wakemeup.domain.repository.Repository;

public class Sdk {

    private static Repository INSTANCE;

    private Sdk(){
    }

    public static synchronized Repository init(@NonNull Context context){
        if (INSTANCE == null) {
            INSTANCE = new SdkImpl(context);
        }
        return INSTANCE;
    }

    public static Repository instance(){
        return INSTANCE;
    }

}

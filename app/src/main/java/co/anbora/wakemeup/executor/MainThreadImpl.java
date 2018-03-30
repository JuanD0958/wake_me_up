package co.anbora.wakemeup.executor;

import android.os.Handler;

import co.anbora.wakemeup.domain.usecase.UseCaseUiThreadPool;


/**
 * Created by NoaD on 01/05/2017.
 */

public class MainThreadImpl implements UseCaseUiThreadPool {

    private static MainThreadImpl mainThread;

    private static class SingletonHelper {
        private static final MainThreadImpl INSTANCE = new MainThreadImpl();
    }

    public static MainThreadImpl getInstance(){

        return SingletonHelper.INSTANCE;
    }

    private final Handler mHandler = new Handler();

    private MainThreadImpl(){}

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}

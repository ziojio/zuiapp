package uiapp.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;


@Singleton
@Component
public interface AppDI {
    @Singleton
    Application app();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application app);

        AppDI build();
    }
}
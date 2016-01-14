package com.jtechme.jumpgokids.app;

import javax.inject.Singleton;

import com.jtechme.jumpgokids.activity.BrowserActivity;
import com.jtechme.jumpgokids.constant.BookmarkPage;
import com.jtechme.jumpgokids.dialog.BookmarksDialogBuilder;
import com.jtechme.jumpgokids.fragment.BookmarkSettingsFragment;
import com.jtechme.jumpgokids.fragment.BookmarksFragment;
import com.jtechme.jumpgokids.object.SearchAdapter;
import dagger.Component;

/**
 * Created by Stefano Pacifici on 01/09/15.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(BrowserActivity activity);

    void inject(BookmarksFragment fragment);

    void inject(BookmarkSettingsFragment fragment);

    void inject(SearchAdapter adapter);

    void inject(BookmarksDialogBuilder builder);

    void inject(BookmarkPage bookmarkPage);
}

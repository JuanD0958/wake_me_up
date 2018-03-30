package co.anbora.wakemeup.ui.about;

import android.content.Context;

import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import co.anbora.wakemeup.R;

public class AboutFragment extends MaterialAboutFragment {

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {

        return new MaterialAboutList.Builder()
                .addCard(addApplicationCard())
                .addCard(addAuthorCard())
                .build();
    }

    private MaterialAboutCard addApplicationCard() {

        return new MaterialAboutCard.Builder()
                .addItem(addApplicationItemName())
                .addItem(addApplicationItemVersion())
                .build();
    }

    private MaterialAboutItem addApplicationItemName() {

        return new MaterialAboutTitleItem.Builder()
                .icon(R.mipmap.ic_launcher)
                .text(R.string.app_name)
                .build();
    }

    private MaterialAboutItem addApplicationItemVersion() {

        return new MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_info_outline_black_24dp)
                .text(R.string.version)
                .subText(R.string.nro_version)
                .build();
    }

    private MaterialAboutCard addAuthorCard() {
        return new MaterialAboutCard.Builder()
                .title(R.string.team)
                .addItem(addDeveloperName())
                .addItem(addGithubRepository())
                .build();
    }

    private MaterialAboutItem addDeveloperName() {

        return new MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_person_pin_black_24dp)
                .text(R.string.author)
                .subText(R.string.country)
                .build();
    }

    private MaterialAboutItem addGithubRepository() {

        return new MaterialAboutActionItem.Builder()
                .icon(R.drawable.github_mark)
                .text(R.string.fork_on_github)
                .build();
    }

    @Override
    protected int getTheme() {
        return R.style.AppTheme_MaterialAboutActivity_Fragment;
    }
}

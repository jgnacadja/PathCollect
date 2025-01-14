/*
 * Copyright 2018 Shobhit Agarwal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.odk.collect.android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.AboutListAdapter;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.android.utilities.ExternalWebPageHelper;
import org.odk.collect.androidshared.system.IntentLauncher;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import javax.inject.Inject;

public class AboutActivity extends CollectAbstractActivity implements
        AboutListAdapter.AboutItemClickListener {

    private static final String LICENSES_HTML_PATH = "file:///android_asset/open_source_licenses.html";
    private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";

    private ExternalWebPageHelper websiteTabHelper;
    private ExternalWebPageHelper forumTabHelper;
    private Uri websiteUri;
    private Uri forumUri;

    @Inject
    IntentLauncher intentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar(getString(R.string.about_preferences), false, null);

        int[][] items = {
                {R.drawable.ic_outline_website_24, R.string.odk_website, R.string.odk_website_summary},
                {R.drawable.ic_outline_share_24, R.string.tell_your_friends, R.string.tell_your_friends_msg},
                {R.drawable.ic_outline_stars_24, R.string.all_open_source_licenses, R.string.all_open_source_licenses_msg},
                {R.drawable.vector, R.string.download_a_survey_form, R.string.download_a_survey_form_msg},
                {R.drawable.vector, R.string.how_do_i_respond_to_a_survey_form, R.string.how_do_i_respond_to_a_survey_form_msg},
                {R.drawable.vector, R.string.how_do_i_submit_a_saved_form, R.string.how_do_i_submit_a_saved_form_msg},
                {R.drawable.vector, R.string.how_can_i_access_the_discussion_forum, R.string.how_can_i_access_the_discussion_forum_msg},

        };

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AboutListAdapter(items, this, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        websiteTabHelper = new ExternalWebPageHelper();
        forumTabHelper = new ExternalWebPageHelper();

        websiteUri = Uri.parse(getString(R.string.app_url));
        forumUri = Uri.parse(getString(R.string.forum_url));
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            switch (position) {
                case 0:
                    websiteTabHelper.openWebPageInCustomTab(this, websiteUri);
                    break;
                case 1:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.tell_your_friends_msg) + " " + GOOGLE_PLAY_URL
                                    + getPackageName());
                    startActivity(Intent.createChooser(shareIntent,
                            getString(R.string.tell_your_friends)));
                    break;
                case 2:
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra(ExternalWebPageHelper.OPEN_URL, LICENSES_HTML_PATH);
                    startActivity(intent);
                    break;


            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        websiteTabHelper.bindCustomTabsService(this, websiteUri);
        forumTabHelper.bindCustomTabsService(this, forumUri);
    }

    @Override
    public void onDestroy() {
        unbindService(websiteTabHelper.getServiceConnection());
        unbindService(forumTabHelper.getServiceConnection());
        super.onDestroy();
    }
}

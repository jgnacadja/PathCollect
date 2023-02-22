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
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.TopicListAdapter;
import org.odk.collect.android.adapters.model.Topic;
import org.odk.collect.android.formlists.blankformlist.BlankFormListActivity;
import org.odk.collect.android.injection.DaggerUtils;
import org.odk.collect.androidshared.system.IntentLauncher;
import org.odk.collect.androidshared.ui.multiclicksafe.MultiClickGuard;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TopicActivity extends CollectAbstractActivity implements
        TopicListAdapter.TopicItemClickListener {

    @Inject
    IntentLauncher intentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);
        DaggerUtils.getComponent(this).inject(this);

        initToolbar();

        List<Topic> topics = new ArrayList<Topic>();
        topics.add(new Topic("1", "Avortement", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse1.mm.bing.net%2Fth%3Fid%3DOIP.XrKyI5FPoD_1qiBK4NltAQHaHs%26pid%3DApi&f=1&ipt=84638f0dc938d8e0901ca7f0a2f41868ed602e7e6435c121edd3e3fb0fb1bc29&ipo=images"));
        topics.add(new Topic("2", "Contraception familiale", "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse3.mm.bing.net%2Fth%3Fid%3DOIP.SszthSkd5uWhaCx-HYx5yQHaHa%26pid%3DApi&f=1&ipt=7a4d75712bcb0170a818902a5d0a1b0caab36a0940a51ef34b9602e06fc03136&ipo=images"));

        RecyclerView recyclerView = findViewById(R.id.topicRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TopicListAdapter(topics, this, this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle(getString(R.string.collect_app_name));
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(int position) {
        if (MultiClickGuard.allowClick(getClass().getName())) {
            switch (position) {
                case 0:
                    Intent i1 = new Intent(this, BlankFormListActivity.class);
                    startActivity(i1);
                    break;
                case 1:
                    Intent i2 = new Intent(this, AboutActivity.class);
                    startActivity(i2);
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

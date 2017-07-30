// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.wdziemia.githubtimes.ui.repository.web;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wdziemia.githubtimes.R;
import com.wdziemia.githubtimes.util.StringUtil;

/**
 * A BroadcastReceiver that handles the Action Intent from the Custom Tab and shows a chooser to
 * share the URL
 */
public class ChromeTabsBroadcastReceiver extends BroadcastReceiver {

    public static final int ACTION_SHARE = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();
        if (!StringUtil.isEmpty(url)) {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);

            Intent chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.share_title));
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);
        }
    }
}
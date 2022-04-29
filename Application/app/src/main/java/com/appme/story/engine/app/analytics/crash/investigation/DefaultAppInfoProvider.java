package com.appme.story.engine.app.analytics.crash.investigation;


import android.content.Context;

import com.appme.story.engine.app.utils.AppInfoUtil;

public class DefaultAppInfoProvider implements AppInfoProvider {
  private final Context context;

  public DefaultAppInfoProvider(Context context) {
    this.context = context;
  }

  @Override
  public AppInfo getAppInfo() {
    return new AppInfo.Builder()
        .with("Version", AppInfoUtil.getAppVersion(context))
        .build();
  }
}

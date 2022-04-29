package com.appme.story.engine.app.listeners;

import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.models.AppInfoViewModel;

public interface OnCrashHandlerListener {
    
    void openSendApplicationChooser(String crashDetails);

    void renderAppInfo(AppInfoViewModel viewModel);

    void render(CrashViewModel viewModel);
    
}

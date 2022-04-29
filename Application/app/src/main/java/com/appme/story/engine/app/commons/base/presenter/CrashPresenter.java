package com.appme.story.engine.app.commons.base.presenter;

import com.appme.story.engine.app.analytics.crash.database.CrashDatabaseHelper;
import com.appme.story.engine.app.analytics.crash.investigation.Crash;
import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.listeners.OnCrashHandlerListener;

public class CrashPresenter {
    private final CrashDatabaseHelper database;
    private final OnCrashHandlerListener mOnCrashHandlerListener;

    public CrashPresenter(CrashDatabaseHelper database, OnCrashHandlerListener onCrashHandlerListener) {
        this.database = database;
        this.mOnCrashHandlerListener = onCrashHandlerListener;
    }

    public void render(int crashId) {
        Crash crash = database.getCrashById(crashId);
        CrashViewModel crashViewModel = new CrashViewModel(crash);

        mOnCrashHandlerListener.render(crashViewModel);
        mOnCrashHandlerListener.renderAppInfo(crashViewModel.getAppInfoViewModel());
    }

    public void shareCrashDetails(CrashViewModel viewModel) {
        mOnCrashHandlerListener.openSendApplicationChooser(viewModel.getCrashInfo());
    }
}

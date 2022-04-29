package com.appme.story.engine.app.commons.base.presenter;

import com.appme.story.engine.app.analytics.crash.database.CrashDatabaseHelper;
import com.appme.story.engine.app.analytics.crash.investigation.Crash;
import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.models.CrashesViewModel;
import com.appme.story.engine.app.listeners.OnCrashActionListener;

import java.util.ArrayList;
import java.util.List;

public class CrashListPresenter {
    private final OnCrashActionListener mOnCrashActionListener;

    public CrashListPresenter(OnCrashActionListener onCrashActionListener) {
        this.mOnCrashActionListener = onCrashActionListener;
    }

    public void render(CrashDatabaseHelper database) {
        List<Crash> crashes = database.getCrashes();
        ArrayList<CrashViewModel> crashViewModels = new ArrayList<>();
        for (Crash crash : crashes) {
            crashViewModels.add(new CrashViewModel(crash));
        }
        mOnCrashActionListener.render(new CrashesViewModel(crashViewModels));
    }

    public void onCrashClicked(CrashViewModel viewModel) {
        mOnCrashActionListener.openCrashDetails(viewModel.getIdentifier());
    }
}

package com.appme.story.engine.app.models;

import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.utils.ViewState;

import java.util.List;

public class CrashesViewModel {
  private final List<CrashViewModel> crashViewModels;

  public CrashesViewModel(List<CrashViewModel> crashViewModels) {
    this.crashViewModels = crashViewModels;
  }

  public List<CrashViewModel> getCrashViewModels() {
    return crashViewModels;
  }

  public ViewState getCrashNotFoundViewState() {
    return new ViewState.Builder().withVisible(crashViewModels.isEmpty()).build();
  }
}

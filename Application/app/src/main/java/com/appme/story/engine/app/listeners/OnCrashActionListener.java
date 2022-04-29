package com.appme.story.engine.app.listeners;

import com.appme.story.engine.app.models.CrashesViewModel;

public interface OnCrashActionListener {
    
  void render(CrashesViewModel viewModel);

  void openCrashDetails(int crashId);
}

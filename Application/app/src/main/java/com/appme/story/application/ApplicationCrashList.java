package com.appme.story.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.appme.story.R;
import com.appme.story.engine.app.commons.base.activity.BaseActivity;
import com.appme.story.engine.app.commons.base.presenter.CrashListPresenter;
import com.appme.story.engine.app.analytics.crash.database.CrashDatabaseHelper;
import com.appme.story.engine.app.adapters.CrashAdapter;
import com.appme.story.engine.app.models.CrashesViewModel;
import com.appme.story.engine.app.listeners.OnCrashActionListener;

public class ApplicationCrashList extends BaseActivity implements OnCrashActionListener {

  private CrashListPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_application_crash_listeners);

    enableHomeButton((Toolbar) findViewById(R.id.toolbar));
    setTitle(R.string.app_name);

    CrashDatabaseHelper database = new CrashDatabaseHelper(this);
    presenter = new CrashListPresenter(this);
    presenter.render(database);
  }

  @Override
  public void render(CrashesViewModel viewModel) {
    RecyclerView crashList = (RecyclerView) findViewById(R.id.crash_list);
    CrashAdapter crashAdapter = new CrashAdapter(viewModel.getCrashViewModels(), presenter);
    crashList.setAdapter(crashAdapter);
    crashList.setLayoutManager(new LinearLayoutManager(this));

    LinearLayout noCrashFoundLayout = (LinearLayout) findViewById(R.id.no_crash_found_layout);
    //noinspection WrongConstant
    noCrashFoundLayout.setVisibility(viewModel.getCrashNotFoundViewState().getVisibility());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_application_crash_github, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.github_link) {
      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.crash_github_link)));
      startActivity(browserIntent);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void openCrashDetails(int crashId) {
    Intent intent = new Intent(this, ApplicationCrashHandler.class);
    intent.putExtra(ApplicationCrashHandler.CRASH_ID, crashId);

    startActivity(intent);
  }
}

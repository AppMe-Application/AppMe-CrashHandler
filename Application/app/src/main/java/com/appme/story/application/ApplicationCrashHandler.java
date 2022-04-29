package com.appme.story.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.appme.story.R;
import com.appme.story.engine.app.commons.base.activity.BaseActivity;
import com.appme.story.engine.app.commons.base.presenter.CrashPresenter;
import com.appme.story.engine.app.analytics.crash.database.CrashDatabaseHelper;
import com.appme.story.engine.app.analytics.crash.investigation.CrashViewModel;
import com.appme.story.engine.app.adapters.AppInfoAdapter;
import com.appme.story.engine.app.models.AppInfoViewModel;
import com.appme.story.engine.app.listeners.OnCrashHandlerListener;

public class ApplicationCrashHandler extends BaseActivity implements OnCrashHandlerListener {
    
  public static final String CRASH_ID = "com.appme.story.CRASH_ID";
  private CrashViewModel viewModel = new CrashViewModel();
  private CrashPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    int crashId = intent.getIntExtra(CRASH_ID, -1);
    setContentView(R.layout.activity_application_crashhandler);

    enableHomeButton((Toolbar) findViewById(R.id.toolbar));
    setTitle(R.string.crash_report);

    presenter = new CrashPresenter(new CrashDatabaseHelper(this), this);
    presenter.render(crashId);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_application_crash, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.share) {
      presenter.shareCrashDetails(viewModel);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void render(CrashViewModel viewModel) {
    this.viewModel = viewModel;
    TextView crashLocation = (TextView) findViewById(R.id.crash_location);
    TextView crashReason = (TextView) findViewById(R.id.crash_reason);
    TextView stackTrace = (TextView) findViewById(R.id.stacktrace);

    crashLocation.setText(viewModel.getExactLocationOfCrash());
    crashReason.setText(viewModel.getReasonOfCrash());
    stackTrace.setText(viewModel.getStackTrace());

    renderDeviceInfo(viewModel);
  }

  @Override
  public void openSendApplicationChooser(String crashDetails) {
    Intent share = new Intent(Intent.ACTION_SEND);
    share.setType("text/plain");
    share.putExtra(Intent.EXTRA_TEXT, crashDetails);

    startActivity(Intent.createChooser(share, getString(R.string.crash_share_dialog_message)));
  }

  @Override
  public void renderAppInfo(AppInfoViewModel viewModel) {
    RecyclerView appInfoDetails = (RecyclerView) findViewById(R.id.app_info_details);
    appInfoDetails.setAdapter(new AppInfoAdapter(viewModel));
    appInfoDetails.setLayoutManager(new LinearLayoutManager(this));
  }

  private void renderDeviceInfo(CrashViewModel viewModel) {
    TextView deviceName = (TextView) findViewById(R.id.device_name);
    TextView deviceBrand = (TextView) findViewById(R.id.device_brand);
    TextView deviceAndroidVersion = (TextView) findViewById(R.id.device_android_version);

    deviceName.setText(viewModel.getDeviceName());
    deviceBrand.setText(viewModel.getDeviceBrand());
    deviceAndroidVersion.setText(viewModel.getDeviceAndroidApiVersion());
  }
}

package com.appme.story.engine.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appme.story.R;
import com.appme.story.engine.app.models.AppInfoRowViewModel;
import com.appme.story.engine.app.models.AppInfoViewModel;

import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {
  private final List<AppInfoRowViewModel> appInfoViewModels;

  public AppInfoAdapter(AppInfoViewModel appInfoViewModel) {
    this.appInfoViewModels = appInfoViewModel.getAppInfoRowViewModels();
  }

  @Override
  public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    LinearLayout appInfoView = (LinearLayout) inflater.inflate(R.layout.item_crash_app_info, parent, false);
    return new AppInfoViewHolder(appInfoView);
  }

  @Override
  public void onBindViewHolder(AppInfoViewHolder holder, int position) {
    holder.render(appInfoViewModels.get(position));
  }

  @Override
  public int getItemCount() {
    return appInfoViewModels.size();
  }
}

class AppInfoViewHolder extends RecyclerView.ViewHolder {
  private final LinearLayout rootView;

  AppInfoViewHolder(LinearLayout rootView) {
    super(rootView);
    this.rootView = rootView;
  }

  void render(AppInfoRowViewModel appInfoViewModel) {
    TextView appInfoAttr = (TextView) rootView.findViewById(R.id.app_info_attr);
    TextView appInfoVal = (TextView) rootView.findViewById(R.id.app_info_val);

    appInfoAttr.setText(appInfoViewModel.getAttr());
    appInfoVal.setText(appInfoViewModel.getVal());
  }
}


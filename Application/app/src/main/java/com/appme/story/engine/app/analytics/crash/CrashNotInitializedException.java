package com.appme.story.engine.app.analytics.crash;

public class CrashNotInitializedException extends RuntimeException {
  public CrashNotInitializedException() {
    super("Initialize Sherlock using Sherlock.init(context) before using its methods");
  }
}

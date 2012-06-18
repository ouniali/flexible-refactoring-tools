// Copyright 2012 Google Inc. All Rights Reserved.

package history.interfaces;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public interface IWorkQueue {
  public void enqueue(Runnable runnable);
  public int getLength();
  public void start();
  public void pause() throws Exception;
  public void resume();
}

// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;

import org.eclipse.jdt.core.ICompilationUnit;

import history.interfaces.ICompilationUnitHistoryRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import history.interfaces.IWorkQueue;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class HistoryWorkQueue implements IWorkQueue, Runnable{

  private static IWorkQueue queue;
  
  public static void handleNewVersion(final ICompilationUnit unit)
  {
    IWorkQueue q = getHistoryWorkQueueInstance();
    q.enqueue(
       new Runnable(){
        @Override
        public void run() {
            ICompilationUnitHistoryRecord record;
            try {
              record = CompilationUnitHistoryRecord.createCompilationUnitHistoryRecord(unit);
              CompilationUnitHistoryCollector.getInstance().addHistoryRecord(record);
            } catch (IOException e) {
              e.printStackTrace();
            }
        }      
       }
     );
  }
 
  
  private static IWorkQueue getHistoryWorkQueueInstance()
  {
    if(queue == null)
      queue = new HistoryWorkQueue();
    return queue;
  }
  
  private final List<Runnable> tasks;
  private final Thread handler;
  private final long SLEEPTIME;
  
  private HistoryWorkQueue()
  {
    tasks = Collections.synchronizedList(new ArrayList<Runnable>());
    handler = new Thread(this);
    SLEEPTIME = 100;
  }
  
  @Override
  public synchronized void enqueue(Runnable runnable) {
    tasks.add(runnable);
  }

  @Override
  public int getLength() {
    return tasks.size();
  }

  @Override
  public void start() {
      handler.start();
  }

  @Override
  public void pause() throws Exception {
      handler.sleep(0);
  }

  @Override
  public void resume() {
    handler.resume();
  }

  @Override
  public void run() {
     for(Runnable r : tasks)
     {
       r.run();
       try {
        Thread.sleep(SLEEPTIME);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
     }
  }

}

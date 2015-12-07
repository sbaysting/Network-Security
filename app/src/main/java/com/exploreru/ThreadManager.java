package com.exploreru;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SAM on 7/25/2015.
 */
public class ThreadManager{

    protected static List<Thread> aliveThreads = new ArrayList<Thread>();
    protected static List<Thread> aliveUIThreads = new ArrayList<Thread>();
    protected static List<Thread> orderedThreads = new ArrayList<Thread>();
    protected static List<Thread> orderedUIThreads = new ArrayList<Thread>();
    protected static HashMap<String, Thread> aliveNamedThreads = new HashMap<String, Thread>();
    protected static HashMap<String, Thread> aliveNamedUIThreads = new HashMap<String, Thread>();

    private static Thread listChecker = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!aliveThreads.isEmpty()) {
                
            }
        }
    });

    private static Thread listUIChecker = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });

    /**
     * Run this thread immediately as a new thread
     * @param run
     */
    public static void asyncExec(Runnable run){
        asyncExec(run, null);
    }

    /**
     * Run this thread immediately as a new thread
     * Give a name to the thread for accessing it later
     * @param run
     * @param name
     */
    public static void asyncExec(Runnable run, String name) {
        Thread t = new Thread(run);
        t.setName(name);
        aliveThreads.add(t);
        if (name != null) {
            aliveNamedThreads.put(name,t);
        }
        t.start();
        run();
    }

    /**
     * Run this thread immediately and wait for it to finish
     * @param run
     */
    public static int syncExec(Runnable run){
        Thread t = new Thread(run);
        t.start();
        try {
            t.join();
            return 0;
        } catch (Exception e){
            Log.e("Thread Interrupted", e.toString());
            return 1;
        }
    }

    /**
     * Run this thread in an ordered fashion with the lowest priority
     * This appends the thread to the back of the ready queue
     * @param run
     */
    public static void orderedExec(Runnable run){
        orderedExec(run, 0, null);
    }

    /**
     * Run this thread in an ordered fashion with the lowest priority
     * This appends the thread to the back of the ready queue
     * Give a name parameter to access the thread later
     * @param run
     * @param name
     */
    public static void orderedExec(Runnable run, String name){
        orderedExec(run, 0, name);
    }

    /**
     * Run this thread in an ordered fashion with a given priority
     * This will insert this thread into the list based on the given priority
     * @param run
     * @param priority
     */
    public static void orderedExec(Runnable run, int priority){
        orderedExec(run, priority, null);
    }

    /**
     * Run this thread in an ordered fashion with a given priority
     * This will insert this thread into the list based on the given priority
     * Give a name parameter to access the thread later
     * @param run
     * @param priority
     * @param name
     */
    public static void orderedExec(Runnable run, int priority, String name){
        Thread t = new Thread(run);
        t.setName(name);
        aliveThreads.add(t);
        if(priority <= 0){
            orderedThreads.add(t);
        } else {
            if(orderedThreads.size() <= priority){
                orderedThreads.add(t);
            } else {
                orderedThreads.add(priority-1,t);
            }
        }
        if(name != null){
            aliveNamedThreads.put(name,t);
        }
        t.start();
        run();
    }

    /**
     * Run this thread asap on the UI thread
     * @param run
     */
    public static void asyncUIExec(Runnable run){
        asyncUIExec(run, null);
    }

    /**
     * Run this thread asap on the UI thread
     * Give a name to the thread for accessing it later
     * @param run
     * @param name
     */
    public static void asyncUIExec(Runnable run, String name){
        Thread t = new Thread(run);
        t.setName(name);
        aliveUIThreads.add(t);
        if (name != null) {
            aliveNamedUIThreads.put(name,t);
        }
        t.start();
        runUI();
    }

    /**
     * Run this thread on the UI thread asap and wait for it to finish
     * @param run
     */
    public static int syncUIExec(Runnable run){
        Thread t = new Thread(run);
        t.start();
        try {
            t.join();
            return 0;
        } catch (Exception e){
            Log.e("Thread Interrupted", e.toString());
            return 1;
        }
    }

    /**
     * Run this thread on the UI thread in an ordered fashion with the lowest priority
     * This appends the thread to the back of the ready queue
     * @param run
     */
    public static void orderedUIExec(Runnable run){
        orderedUIExec(run, 0, null);
    }

    /**
     * Run this thread on the UI thread in an ordered fashion with the lowest priority
     * This appends the thread to the back of the ready queue
     * Give a name parameter to access the thread later
     * @param run
     * @param name
     */
    public static void orderedUIExec(Runnable run, String name){
        orderedUIExec(run, 0, name);
    }

    /**
     * Run this thread on the UI thread in an ordered fashion with a given priority
     * This will insert this thread into the list based on the given priority
     * @param run
     * @param priority
     */
    public static void orderedUIExec(Runnable run, int priority){
        orderedUIExec(run, priority, null);
    }

    /**
     * Run this thread on the UI thread in an ordered fashion with a given priority
     * This will insert this thread into the list based on the given priority
     * Give a name parameter to access the thread later
     * @param run
     * @param priority
     * @param name
     */
    public static void orderedUIExec(Runnable run, int priority, String name){
        Thread t = new Thread(run);
        t.setName(name);
        aliveUIThreads.add(t);
        if(priority <= 0){
            orderedUIThreads.add(t);
        } else {
            if(orderedUIThreads.size() <= priority){
                orderedUIThreads.add(t);
            } else {
                orderedUIThreads.add(priority-1,t);
            }
        }
        if(name != null){
            aliveNamedUIThreads.put(name,t);
        }
        t.start();
        runUI();
    }

    /**
     * Check and remove dead threads from the lists
     */
    private static synchronized void run(){
        if(!listChecker.isAlive()){
            listChecker.start();
        }
    }

    /**
     * Check and remove dead threads from the UI lists
     */
    private static synchronized void runUI(){
        if(!listUIChecker.isAlive()){
            listUIChecker.start();
        }
    }
}

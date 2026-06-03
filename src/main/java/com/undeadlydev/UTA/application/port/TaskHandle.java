package com.undeadlydev.UTA.application.port;

/** A cancellable scheduled task, decoupling the application from the concrete scheduler library. */
public interface TaskHandle {

    void cancel();
}

// IMockService.aidl
package com.shenyu.mock;

// Declare any non-default types here with import statements
import com.shenyu.mock.ILifecycleCallback;

interface IMockService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void startServer();

    void stopServer();

    void registerServerLifecycle(ILifecycleCallback callback);

    void unregisterServerLifecycle(ILifecycleCallback callback);
}
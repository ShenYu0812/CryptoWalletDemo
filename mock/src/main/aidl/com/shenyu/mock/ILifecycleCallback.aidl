// IMockService.aidl
package com.shenyu.mock;

oneway interface ILifecycleCallback {
    void onLifecycleEvent(int lifecycleState);
}
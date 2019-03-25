package com.agilie.adssampleapp.app

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.agilie.adssampleapp.leadbolt.OverriddenLeadboltModuleActivity

class AdsSampleLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity?) {
        Log.d("TEST_LIFECYCLE", "PAUSED -> activity ${activity!!::class.java.canonicalName}")
    }

    override fun onActivityResumed(activity: Activity?) {
        Log.d("TEST_LIFECYCLE", "RESUMED -> activity ${activity!!::class.java.canonicalName}")
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d("TEST_LIFECYCLE", "STARTED -> activity ${activity!!::class.java.canonicalName}")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d("TEST_LIFECYCLE", "DESTROYED -> activity ${activity!!::class.java.canonicalName}")

        val activityListener = activity as? AdsSampleApplication.LeadboltActivityBackPressListener
        if (activityListener != null && activityListener == AdsSampleApplication.backPressListener) {
            AdsSampleApplication.backPressListener = null
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.d("TEST_LIFECYCLE", "SAVE_INSTANCE_STATE -> activity ${activity!!::class.java.canonicalName}")
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d("TEST_LIFECYCLE", "STOPPED -> activity ${activity!!::class.java.canonicalName}")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d("TEST_LIFECYCLE", "CREATED -> activity ${activity!!::class.java.canonicalName}")
        if ("com.apptracker.android.module.AppModuleActivity" == activity::class.java.canonicalName) {
            activity.startActivity(Intent(activity, OverriddenLeadboltModuleActivity::class.java))
            activity.finish()
        }
    }
}

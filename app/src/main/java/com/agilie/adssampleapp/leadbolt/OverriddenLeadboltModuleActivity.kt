package com.agilie.adssampleapp.leadbolt

import com.agilie.adssampleapp.app.AdsSampleApplication
import com.apptracker.android.module.AppModuleActivity

open class OverriddenLeadboltModuleActivity : AppModuleActivity() {

    override fun onBackPressed() {
        AdsSampleApplication.backPressListener?.onAdBackPressed()
        super.onBackPressed()
    }
}
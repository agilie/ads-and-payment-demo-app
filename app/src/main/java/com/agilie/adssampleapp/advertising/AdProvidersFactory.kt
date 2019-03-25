package com.agilie.adssampleapp.advertising

import com.agilie.adssampleapp.adform.AdFormAdProvider
import com.agilie.adssampleapp.inmobi.InMobiAdProvider
import com.agilie.adssampleapp.leadbolt.LeadboltAdProvider

abstract class AdProvidersFactory {
    companion object {
        @JvmStatic
        fun getProvider(type: AdProvidersType): AdProvider {
            return when (type) {
                AdProvidersType.AD_FORM -> AdFormAdProvider()
                AdProvidersType.IN_MOBI -> InMobiAdProvider()
                AdProvidersType.LEADBOLT -> LeadboltAdProvider()
            }
        }
    }
}
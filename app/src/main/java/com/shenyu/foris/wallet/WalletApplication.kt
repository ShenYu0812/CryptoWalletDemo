package com.shenyu.foris.wallet

import android.app.Application
import com.blankj.utilcode.util.Utils


class WalletApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this@WalletApplication)
    }
}
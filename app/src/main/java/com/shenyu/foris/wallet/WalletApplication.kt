package com.shenyu.foris.wallet

import android.app.Application
import com.blankj.utilcode.util.Utils
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security


class WalletApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this@WalletApplication)
        Security.removeProvider("BC") // 先移除已存在的BC提供者
        Security.addProvider(BouncyCastleProvider())
    }

}

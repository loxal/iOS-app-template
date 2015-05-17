/*
 * Copyright 2015 Alexander Orlov <alexander.orlov@loxal.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.loxal.user.ios

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import net.loxal.user.ios.view.RootViewController
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.uikit.*
import org.robovm.objc.annotation.Property
import java.util.logging.Logger
import kotlin.platform.platformStatic

class App : UIApplicationDelegateAdapter() {
    Property(selector = "window")
    private final var window: UIWindow? = getWindow()

    override fun didFinishLaunching(app: UIApplication?, launchOptions: UIApplicationLaunchOptions?): Boolean {
        configureWindow()

        return true
    }

    private fun configureWindow() {
        window?.setRootViewController(RootViewController())
        window?.makeKeyAndVisible()
    }

    override fun getWindow() = window

    override fun setWindow(window: UIWindow?) {
        this.window = UIWindow(UIScreen.getMainScreen().getNativeBounds())
    }

    companion object {
        val LOG: Logger = Logger.getGlobal()
        val MAPPER = ObjectMapper()

        init {
            configureMapper()
        }

        private fun configureMapper() {
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        platformStatic fun main(vararg args: String) {
            val autoreleasePool = NSAutoreleasePool()
            UIApplication.main<UIApplication, App>(args, null, javaClass<App>())
            autoreleasePool.close()
        }
    }
}
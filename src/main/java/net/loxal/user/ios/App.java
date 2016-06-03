/*
 * Copyright 2016 Alexander Orlov <alexander.orlov@loxal.net>
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

package net.loxal.user.ios;

import com.bugvm.apple.foundation.NSAutoreleasePool;
import com.bugvm.apple.uikit.UIApplication;
import com.bugvm.apple.uikit.UIApplicationDelegateAdapter;
import com.bugvm.apple.uikit.UIApplicationLaunchOptions;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.loxal.user.ios.view.RootViewController;

import java.util.logging.Logger;

public class App extends UIApplicationDelegateAdapter {
    public static final Logger LOG = Logger.getGlobal();
    public static final ObjectMapper MAPPER = new ObjectMapper();

    {
        configureMapper();
    }

    public static void main(String... args) {
        NSAutoreleasePool autoreleasePool = new NSAutoreleasePool();
        UIApplication.main(args, null, App.class);
        autoreleasePool.close();
    }

    @Override
    public boolean didFinishLaunching(UIApplication app, UIApplicationLaunchOptions launchOptions) {
        new RootViewController();
        return true;
    }

    private void configureMapper() {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}

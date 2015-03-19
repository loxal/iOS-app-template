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

package net.loxal.user.ios.view

import org.robovm.apple.coregraphics.CGRect
import org.robovm.apple.spritekit.SKView
import org.robovm.apple.uikit.UIImage
import org.robovm.apple.uikit.UIImageView
import org.robovm.apple.uikit.UIScreen
import org.robovm.apple.uikit.UIViewController
import org.robovm.objc.annotation.CustomClass

CustomClass("RootViewController")
public class RootViewController : UIViewController() {
    private val mainView = getView()
    private val skView: SKView
    private val logo: UIImageView

    init {
        skView = SKView()
        skView.setFrame(UIScreen.getMainScreen().getApplicationFrame())
        mainView.addSubview(skView)

        logo = UIImageView(CGRect(5.0, 10.0, 375.0, 220.0))
        logo.setImage(UIImage.create("logo.png"))
        skView.addSubview(logo)

        //        skView.text
    }
}
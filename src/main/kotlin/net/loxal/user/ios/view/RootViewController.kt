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

import org.robovm.apple.coregraphics.CGPoint
import org.robovm.apple.coregraphics.CGSize
import org.robovm.apple.spritekit.*
import org.robovm.apple.uikit.UIColor
import org.robovm.apple.uikit.UIScreen
import org.robovm.apple.uikit.UIViewController
import org.robovm.objc.annotation.CustomClass

CustomClass("RootViewController")
public class RootViewController : UIViewController() {
    private val mainView = getView()
    private val skView: SKView = SKView()

    init {
        val label = SKLabelNode()
        label.setText("Hello, World!")
        label.setFontSize(65.0)
        label.setFontColor(UIColor.white())
        label.setPosition(CGPoint(400.0, 784.0))

        val rectangle = SKShapeNode()
        rectangle.setPosition(CGPoint(400.0, 784.0))
        rectangle.setGlowWidth(0.5)
        rectangle.setLineWidth(0.5)
        rectangle.setName("OHA")
        rectangle.setFillColor(UIColor.blue())
        //        rectangle.setStrokeColor(UIColor.green())

        val scene = SKScene()
        scene.addChild(rectangle)
        scene.addChild(label)
        scene.setBackgroundColor(UIColor.darkGray())
        scene.setScaleMode(SKSceneScaleMode.AspectFill)
        scene.setSize(CGSize(786.0, 1024.0))


        skView.setFrame(UIScreen.getMainScreen().getApplicationFrame())
        skView.setShowsFPS(true)
        skView.presentScene(scene)

        mainView.addSubview(skView)
    }
}
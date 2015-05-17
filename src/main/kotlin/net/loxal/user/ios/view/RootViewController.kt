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

import net.loxal.user.ios.App
import net.loxal.user.ios.model.Answer
import net.loxal.user.ios.model.Host
import net.loxal.user.ios.model.Question
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.robovm.apple.coregraphics.CGRect
import org.robovm.apple.iad.ADAdType
import org.robovm.apple.iad.ADBannerView
import org.robovm.apple.uikit.*
import org.robovm.objc.annotation.CustomClass
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.Date

CustomClass("RootViewController")
class RootViewController : UIViewController() {
    private val mainView = getView()
    private val infoContainer = UILabel()

    private val questionContainer = UILabel()
    private val answerOption = UIButton.create(UIButtonType.RoundedRect)

    private val timestamp = UILabel()
    private val refresher = UIButton.create(UIButtonType.System)
    private val adBanner = ADBannerView(ADAdType.Banner)

    private val httpClient = DefaultHttpClient()
    private val uri: URI = URI.create("https://api.yaas.io/loxal/rest-kit/v1/who-am-i")
    private val httpGet: HttpGet = HttpGet(uri)

    private val question: Question
    private val answer: Answer

    init {
        mainView.setBackgroundColor(UIColor.white())

        question = Question("How long is a Bavarian Weißwurst sausage?", listOf("10 cm", "20 cm", "30 cm", "impossible to say"))
        answer = Answer("first-question", 2)

        initRefreshUi()
        initQuestionContainer()
        initInfoContainer()
        initInfoTimestamp()
        initAdBanner()
        refreshStatus()
    }

    private fun initQuestionContainer() {
        mainView.addSubview(questionContainer)

        questionContainer.setFrame(CGRect(10.0, 40.0, mainView.getFrame().getMaxX(), 20.0))

        answerOption.setFrame(CGRect(10.0, 60.0, mainView.getFrame().getMaxX(), 20.0))
        answerOption.setTitle(question.answers.get(answer.answerOptionIndex), UIControlState.Normal)
        answerOption.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Left)
        mainView.addSubview(answerOption)
    }

    private fun initInfoTimestamp() {
        mainView.addSubview(timestamp)

        timestamp.setFrame(CGRect(0.0, 120.0, mainView.getFrame().getMaxX(), 100.0))
        timestamp.setTextAlignment(NSTextAlignment.Center)
        timestamp.setFont(UIFont.getSystemFont(UIFont.getSystemFontSize()))
    }

    private fun initInfoContainer() {
        mainView.addSubview(infoContainer)

        infoContainer.setFrame(CGRect(0.0, 100.0, mainView.getFrame().getMaxX(), 20.0))
        infoContainer.setTextAlignment(NSTextAlignment.Center)
        infoContainer.setFont(UIFont.getSystemFont(UIFont.getSystemFontSize()))
    }

    private fun initRefreshUi() {
        mainView.addSubview(refresher)

        refresher.setFrame(CGRect(0.0, mainView.getFrame().getMidY(), mainView.getFrame().getMaxX() - 10, 30.0))
        refresher.setTitle("Next", UIControlState.Normal)
        refresher.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Center)

        refresher.addOnTouchUpInsideListener({ control, event -> refreshStatus() })
    }

    private fun refreshStatus() = showInfo(fetchHostInfo())

    private fun showInfo(info: String) {
        val host = App.MAPPER.readValue<Host>(info, javaClass<Host>())
        infoContainer.setText("Host name: ${host.name}  IP address: ${host.address}")
        questionContainer.setText(question.question)
        timestamp.setText("Last refresh: ${Date().toGMTString()}")
    }

    private fun fetchHostInfo(): String {
        ByteArrayOutputStream().use { out ->
            val response = httpClient.execute(httpGet)
            val status = response.getStatusLine()
            val entity = response.getEntity()
            if (HttpStatus.SC_OK == status.getStatusCode()) {
                entity.writeTo(out)
                return out.toString()
            } else {
                entity.getContent().close()
            }
        }

        return "I’m very sorry, this should not happen."
    }

    private fun initAdBanner() {
        mainView.addSubview(adBanner)

        adBanner.setFrame(CGRect(0.0, mainView.getFrame().getMaxY() - adBanner.getFrame().getHeight(), 0.0, 0.0))
    }
}
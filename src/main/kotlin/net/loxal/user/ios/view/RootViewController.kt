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

CustomClass("RootViewController")
class RootViewController : UIViewController() {
    private val mainView = getView()

    private val questionContainer = UILabel()
    private val answerOption = UIButton.create(UIButtonType.RoundedRect)

    private val nextQuestion = UIButton.create(UIButtonType.System)
    private val adBanner = ADBannerView(ADAdType.Banner)

    private val httpClient = DefaultHttpClient()
    private val uri: URI = URI.create("https://api.yaas.io/loxal/rest-kit/v1/ballot/poll/simpsons-852812b05-2023-48f2-a88a-6be56f1aa8b2")
    private val httpGet: HttpGet = HttpGet(uri)

    private val answer: Answer

    init {
        mainView.setBackgroundColor(UIColor.white())

        answer = Answer("first-question", 2)

        initQuestionContainer()
        initRefreshUi()
        initAdBanner()
        refreshStatus()
    }

    private fun initQuestionContainer() {
        mainView.addSubview(questionContainer)

        questionContainer.setFont(UIFont.getSystemFont(UIFont.getSystemFontSize()))
        questionContainer.setFrame(CGRect(PADDING, 40.0, mainView.getFrame().getMaxX(), 20.0))

        mainView.addSubview(answerOption)
        answerOption.setFrame(CGRect(PADDING, 80.0, mainView.getFrame().getMaxX(), 20.0))
        answerOption.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Left)
    }

    private fun initRefreshUi() {
        mainView.addSubview(nextQuestion)

        nextQuestion.setFrame(CGRect(0.0, mainView.getFrame().getMaxY() - 200, mainView.getFrame().getMaxX() - PADDING, 20.0))
        nextQuestion.setTitle("Next Question", UIControlState.Normal)
        nextQuestion.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Right)

        nextQuestion.addOnTouchUpInsideListener({ control, event -> refreshStatus() })
    }

    private fun refreshStatus() = showQuestion(fetchQuestion())

    private fun showQuestion(jsonData: String) {
        val question = App.MAPPER.readValue<Question>(jsonData, javaClass<Question>())
        questionContainer.setText(question.question)

        for (answerIdx in question.answers.indices) {
            answerOption.setTitle("${answerIdx + 1}. ${question.answers.get(answerIdx)}", UIControlState.Normal)
        }
    }

    private fun fetchQuestion(): String {
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

    companion object {
        private val PADDING: Double = 10.0
    }
}
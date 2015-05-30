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
import net.loxal.user.ios.model.Poll
import net.loxal.user.ios.model.Vote
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

    private val nextQuestion = UIButton.create(UIButtonType.System)
    private val adBanner = ADBannerView(ADAdType.Banner)

    private val answerContainer = UITableView()

    private val httpClient = DefaultHttpClient()
    private val uri: URI = URI.create("https://api.yaas.io/loxal/rest-kit/v1/ballot/poll/simpsons-852812b05-2023-48f2-a88a-6be56f1aa8b2")
    private val httpGet: HttpGet = HttpGet(uri)

    private val answer: Vote

    init {
        mainView.setBackgroundColor(UIColor.white())

        answer = Vote("first-question", 2)

        initQuestionContainer()
        initNextQuestion()
        initAdBanner()
        refreshStatus()
    }

    private fun initQuestionContainer() {
        mainView.addSubview(questionContainer)

        questionContainer.setFont(UIFont.getSystemFont(UIFont.getSystemFontSize()))
        questionContainer.setFrame(CGRect(PADDING, 40.0, mainView.getFrame().getMaxX(), 20.0))

        mainView.addSubview(answerContainer)
        answerContainer.setFrame(CGRect(PADDING, 80.0, mainView.getFrame().getMaxX() - (PADDING + 5), 400.0))
        answerContainer.setSeparatorStyle(UITableViewCellSeparatorStyle.SingleLine)
    }

    private fun initNextQuestion() {
        mainView.addSubview(nextQuestion)

        nextQuestion.setFrame(CGRect(0.0, mainView.getFrame().getMaxY() - 150, mainView.getFrame().getMaxX() - PADDING, 20.0))
        nextQuestion.setTitle("Next Question", UIControlState.Normal)
        nextQuestion.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Right)

        nextQuestion.addOnTouchUpInsideListener({ control, event -> refreshStatus() })
    }

    private fun refreshStatus() = showQuestion(fetchQuestion())

    private fun showQuestion(jsonData: String) {
        App.LOG.info("next>>>>>>>>>>>>>>>>")
        val poll = App.MAPPER.readValue<Poll>(jsonData, javaClass<Poll>())

        showQuestion(poll)
        showAnswerOptions(poll)
    }

    private fun showQuestion(poll: Poll) {
        questionContainer.setText(poll.question)
    }

    private fun showAnswerOptions(question: Poll) {
        for (answerIdx in question.answers.indices) {
            showAnswerOption(answerIdx, question)
        }
    }

    private fun showAnswerOption(answerIdx: Int, poll: Poll) {
        val answerOption = UIButton.create(UIButtonType.RoundedRect)
        answerOption.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Left)
        val rowIdx = answerIdx + 1
        answerOption.setTitle("${rowIdx}. ${poll.answers.get(answerIdx)}", UIControlState.Normal)
        answerOption.setFrame(CGRect(PADDING, 0.0, mainView.getFrame().getMaxX(), rowIdx * 50.0))
        answerOption.addOnTouchUpInsideListener(UIControl.OnTouchUpInsideListener({ control, event ->
            run {
                App.LOG.info("rowIndex: ${rowIdx}")
            }
        }))

        val answer = UITableViewCell()
        answer.addSubview(answerOption)
        answerContainer.addSubview(answer)
    }

    private fun fetchQuestion(): String {
        ByteArrayOutputStream().use { out ->
            val response = httpClient.execute(httpGet)
            val status = response.getStatusLine()
            val entity = response.getEntity()
            //            if (HttpStatus.SC_OK == status.getStatusCode()) {
                entity.writeTo(out)
                return out.toString()
            //            }
            //            else {
            //                entity.getContent().close()
            //            }
        }

        //        return "I’m very sorry, this should not happen."
    }

    private fun initAdBanner() {
        mainView.addSubview(adBanner)
        adBanner.setFrame(CGRect(0.0, mainView.getFrame().getMaxY() - adBanner.getFrame().getHeight(), 0.0, 0.0))
    }

    companion object {
        private val PADDING: Double = 10.0
    }
}
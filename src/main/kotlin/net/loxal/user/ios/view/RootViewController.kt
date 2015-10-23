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
import net.loxal.user.ios.model.ErrorMessage
import net.loxal.user.ios.model.Poll
import net.loxal.user.ios.model.Vote
import org.apache.http.HttpStatus
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.robovm.apple.coregraphics.CGRect
import org.robovm.apple.iad.ADAdType
import org.robovm.apple.iad.ADBannerView
import org.robovm.apple.uikit.*
import org.robovm.objc.annotation.CustomClass
import org.robovm.objc.annotation.IBAction
import org.robovm.objc.annotation.IBOutlet
import java.io.ByteArrayOutputStream
import java.net.URI
import javax.ws.rs.client.ClientBuilder

@CustomClass("RootViewController")
class RootViewController : UIViewController() {
    @IBOutlet
    private fun setQuestionContainer(label: UILabel) {
        this.questionContainer = label
    }

    @IBAction
    private fun nextQuestion() {
        questionContainer.text = "$uri"
        App.LOG.warning("$uri")

        val c = ClientBuilder.newBuilder().build()
        val u = c.target("https://api.stage.yaas.io/loxal/rest-kit/v1/ballot/poll/simpsons-3801852cf-a0eb-42cd-be59-99f0c55cfa94")
        val rg = u.request().get();
        val o = rg.readEntity(String::class.java)
        val resource = App.MAPPER.readValue<Poll>(o, Poll::class.java)
        questionContainer.text = "${resource.question}"

    }

    private val mainView = view

    private var questionContainer = UILabel()

    private val nextQuestion = UIButton.create(UIButtonType.System)
    private val adBanner = ADBannerView(ADAdType.Banner)

    private val answerContainer = UITableView()

    private val httpClient = DefaultHttpClient()
    private var uri: URI = URI.create("https://api.stage.yaas.io/loxal/rest-kit/v1/ballot/poll/simpsons-42e1dd4d7-32f7-4dd2-8d78-5a94a983360b")
    private val httpGet: HttpGet = HttpGet(uri)

    private val answer: Vote

    init {
        mainView.backgroundColor = UIColor.white()
        //
        answer = Vote("first-question", listOf(2))
        //
        initQuestionContainer()
        initNextQuestion()
        initAdBanner()
        refreshStatus()

        App.LOG.warning("$uri")
        App.LOG.warning("$uri")
        App.LOG.warning("$uri")
        App.LOG.warning("$uri")
    }

    private fun initQuestionContainer() {
        mainView.addSubview(questionContainer)

        questionContainer.font = UIFont.getSystemFont(UIFont.getSystemFontSize())
        questionContainer.frame = CGRect(PADDING, 40.0, mainView.frame.maxX, 20.0)

        mainView.addSubview(answerContainer)
        answerContainer.frame = CGRect(PADDING, 80.0, mainView.frame.maxX - (PADDING + 5), 400.0)
        answerContainer.separatorStyle = UITableViewCellSeparatorStyle.None
    }

    private fun initNextQuestion() {
        mainView.addSubview(nextQuestion)

        nextQuestion.frame = CGRect(0.0, mainView.frame.midY, mainView.frame.midX + 100, 20.0)
        nextQuestion.setTitle("Next Question", UIControlState.Normal)
        nextQuestion.contentHorizontalAlignment = UIControlContentHorizontalAlignment.Right

        nextQuestion.addOnTouchUpInsideListener({ control, event -> refreshStatus() })
    }

    private fun refreshStatus() = showQuestion(fetchResource())

    private fun showQuestion(resource: Any) {
        when (resource) {
            is Poll -> {
                showQuestion(resource)
                showAnswerOptions(resource)
            }
            is ErrorMessage -> questionContainer.text = resource.message
        }
    }

    private fun showQuestion(poll: Poll) {
        questionContainer.text = poll.question
    }

    private fun showAnswerOptions(question: Poll) {
        for (answerIdx in question.options.indices) {
            showAnswerOption(answerIdx, question)
        }
    }

    private fun showAnswerOption(answerIdx: Int, poll: Poll) {
        val answerOption = UIButton.create(UIButtonType.RoundedRect)
        answerOption.contentHorizontalAlignment = UIControlContentHorizontalAlignment.Left
        val rowIdx = answerIdx + 1
        answerOption.setTitle("$rowIdx. ${poll.options[answerIdx]}", UIControlState.Normal)
        answerOption.frame = CGRect(PADDING, 0.0, mainView.frame.maxX, rowIdx * 50.0)
        answerOption.addOnTouchUpInsideListener(UIControl.OnTouchUpInsideListener({ control, event ->
            run {
                App.LOG.info("rowIndex: $rowIdx")
            }
        }))

        val answer = UITableViewCell()
        answer.addSubview(answerOption)
        answerContainer.addSubview(answer)
    }

    private fun fetchResource(): Any {
        ByteArrayOutputStream().use { out ->
            val response = httpClient.execute(httpGet)
            val status = response.statusLine
            val entity = response.entity
            entity.writeTo(out)
            val jsonData = out.toString()
            val resource: Any
            if (HttpStatus.SC_OK == status.statusCode) {
                resource = App.MAPPER.readValue<Poll>(jsonData, Poll::class.java)
            } else {
                resource = App.MAPPER.readValue<ErrorMessage>(jsonData, ErrorMessage::class.java)
            }
            return resource
        }
    }

    private fun initAdBanner() {
        mainView.addSubview(adBanner)
        adBanner.frame = CGRect(0.0, mainView.frame.maxY - adBanner.frame.height, 0.0, 0.0)
    }

    companion object {
        private val PADDING: Double = 10.0

        init {
            App.LOG.warning("App warn")
            App.LOG.info("App info")
        }
    }
}
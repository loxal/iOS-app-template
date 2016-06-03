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

package net.loxal.user.ios.view;

import com.bugvm.apple.coregraphics.CGRect;
import com.bugvm.apple.iad.ADAdType;
import com.bugvm.apple.iad.ADBannerView;
import com.bugvm.apple.uikit.UIButton;
import com.bugvm.apple.uikit.UIButtonType;
import com.bugvm.apple.uikit.UIColor;
import com.bugvm.apple.uikit.UIControlContentHorizontalAlignment;
import com.bugvm.apple.uikit.UIControlState;
import com.bugvm.apple.uikit.UIFont;
import com.bugvm.apple.uikit.UILabel;
import com.bugvm.apple.uikit.UITableView;
import com.bugvm.apple.uikit.UITableViewCell;
import com.bugvm.apple.uikit.UITableViewCellSeparatorStyle;
import com.bugvm.apple.uikit.UIView;
import com.bugvm.apple.uikit.UIViewController;
import com.bugvm.objc.annotation.CustomClass;
import com.bugvm.objc.annotation.IBAction;
import com.bugvm.objc.annotation.IBOutlet;
import net.loxal.user.ios.App;
import net.loxal.user.ios.model.dto.ErrorMessage;
import net.loxal.user.ios.model.dto.Poll;
import net.loxal.user.ios.model.dto.Vote;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@CustomClass("RootViewController")
public class RootViewController extends UIViewController {
    private static final double PADDING = 10.0;

    static {
        App.LOG.warning("App warn");
        App.LOG.info("App info");
    }

    private UIView mainView = getView();

    private UILabel questionContainer = new UILabel();

    private UIButton nextQuestion = new UIButton(UIButtonType.System);
    private ADBannerView adBanner = new ADBannerView(ADAdType.Banner);

    private UITableView answerContainer = new UITableView();

    private DefaultHttpClient httpClient = new DefaultHttpClient();
    private URI uri = URI.create("http://service-kit.loxal.net/ballot/poll/simpsons-17d5313d0-fa6d-470a-af2f-784b1fdcd1af");
    private HttpGet httpGet = new HttpGet(uri);
    private Vote answer;

    {
        mainView.setBackgroundColor(UIColor.white());
        answer = new Vote("first-question", Arrays.asList(2));

        initQuestionContainer();
        initNextQuestion();
        initAdBanner();
        refreshStatus();

        App.LOG.info(uri.toString());
    }

    @IBOutlet
    private void setQuestionContainer(UILabel label) {
        this.questionContainer = label;
    }

    @IBAction
    private void nextQuestion() {
        questionContainer.setText(uri.toString());
        App.LOG.info(uri.toString());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://service-kit.loxal.net/ballot/poll/simpsons-17d5313d0-fa6d-470a-af2f-784b1fdcd1af")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String body = response.body().string();
            App.LOG.info(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initQuestionContainer() {
        mainView.addSubview(questionContainer);

        questionContainer.setFont(UIFont.getSystemFont(UIFont.getSystemFontSize()));
        questionContainer.setFrame(new CGRect(PADDING, 40.0, mainView.getFrame().getMaxX(), 20.0));

        mainView.addSubview(answerContainer);
        answerContainer.setFrame(new CGRect(PADDING, 80.0, mainView.getFrame().getMaxX() - (PADDING + 5), 400.0));
        answerContainer.setSeparatorStyle(UITableViewCellSeparatorStyle.None);
    }

    private void initNextQuestion() {
        mainView.addSubview(nextQuestion);

        nextQuestion.setFrame(new CGRect(0.0, mainView.getFrame().getMidY(), mainView.getFrame().getMidX() + 100, 20.0));
        nextQuestion.setTitle("Next Question", UIControlState.Normal);
        nextQuestion.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Right);

        nextQuestion.addOnTouchUpInsideListener((control, event) -> refreshStatus());
    }

    private void refreshStatus() {
        showQuestion("SHOW!");
    }

    private void showQuestion(Object resource) {
        if (resource instanceof Poll) {
            showQuestion((Poll) resource);
            showAnswerOptions((Poll) resource);
        } else if (resource instanceof ErrorMessage) {
            questionContainer.setText(((ErrorMessage) resource).getMessage());
        }
    }

    private void showQuestion(Poll poll) {
        questionContainer.setText(poll.getQuestion());
    }

    private void showAnswerOptions(Poll question) {
        int idx = 0;
        for (String answerIdx : question.getOptions()) {
            showAnswerOption(idx++, question);
        }
    }

    private void showAnswerOption(int answerIdx, Poll poll) {
        UIButton answerOption = new UIButton(UIButtonType.RoundedRect);
        answerOption.setContentHorizontalAlignment(UIControlContentHorizontalAlignment.Left);
        int rowIdx = answerIdx + 1;
        answerOption.setTitle(poll.getOptions().get(rowIdx), UIControlState.Normal);
        answerOption.setFrame(new CGRect(PADDING, 0.0, mainView.getFrame().getMaxX(), rowIdx * 50.0));
        answerOption.addOnTouchUpInsideListener((uiControl, uiEvent) -> App.LOG.info("rowIndex: " + rowIdx));

        UITableViewCell answer = new UITableViewCell();
        answer.addSubview(answerOption);
        answerContainer.addSubview(answer);
    }

    private void initAdBanner() {
        mainView.addSubview(adBanner);
        adBanner.setFrame(new CGRect(0.0, mainView.getFrame().getMaxY() - adBanner.getFrame().getHeight(), 0.0, 0.0));
    }
}

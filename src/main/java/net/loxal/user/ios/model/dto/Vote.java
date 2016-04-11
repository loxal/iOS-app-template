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

package net.loxal.user.ios.model.dto;

import java.util.List;

public class Vote {
    private String referencePoll = "";
    private String user = "anonymous";
    private boolean correct = false;
    private List<Integer> answers;

    public Vote(String referencePoll, List<Integer> answers) {
        this.referencePoll = referencePoll;
        this.answers = answers;
    }

    public String getReferencePoll() {
        return referencePoll;
    }

    public void setReferencePoll(String referencePoll) {
        this.referencePoll = referencePoll;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public Vote asUser(String referencePoll, List<Integer> answers, String user) {
        Vote vote = new Vote(referencePoll, answers);
        vote.user = user;

        return vote;
    }
}

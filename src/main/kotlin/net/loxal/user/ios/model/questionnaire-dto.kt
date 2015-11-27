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

package net.loxal.user.ios.model

data class Poll(
        var question: String = "",
        var options: List<String> = arrayListOf(),
        /**
         * Provide a hint for the user & UI.
         */
        var multipleAnswers: Boolean = false
)


data class Vote(
        var referencePoll: String = "",
        var answers: List<Int> = arrayListOf()
) {
    var user: String = "anonymous"
    var correct: Boolean? = false

    companion object {
        fun asUser(referencePoll: String, answers: List<Int>, user: String): Vote {
            val vote = Vote(referencePoll = referencePoll, answers = answers)
            vote.user = user

            return vote
        }
    }
}
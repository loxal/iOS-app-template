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

/**
 * Detailed error message used in response to provide all errors triggered by a request.
 */
data class ErrorMessage(
        var type: String? = null
) {
    var status: Int = 0
    var message: String? = ""
    var moreInfo: String = ""
    //    var details: Set<ErrorDetail> = emptySet()

    companion object {
        const val TYPE_REGEXP_PATTERN = "[a-z]+[[a-z]_]*[a-z]+"

        @JvmStatic fun create(errorMsg: String?): ErrorMessage {
            val e = ErrorMessage()
            e.type = errorMsg

            return e
        }
    }
}

data class ErrorDetail(
        var type: String = ""
) {
    var field: String = ""
    var message: String = ""
}

data class Authorization(
        var access_token: String = ""
) {
    var expires_in: Int = 0
    var token_type: String = ""
    var scope: String = ""
}
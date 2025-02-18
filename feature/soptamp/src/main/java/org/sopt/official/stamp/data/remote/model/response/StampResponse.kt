/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.stamp.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.sopt.official.stamp.domain.model.Archive

@Serializable
data class StampResponse(
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("id")
    val id: Int,
    @SerialName("contents")
    val contents: String,
    @SerialName("images")
    val images: List<String>? = null,
    @SerialName("missionId")
    val missionId: Int
) {
    fun toDomain() = Archive(
        createdAt = createdAt,
        updatedAt = updatedAt,
        id = id,
        contents = contents,
        images = images ?: emptyList(),
        missionId = missionId
    )
}

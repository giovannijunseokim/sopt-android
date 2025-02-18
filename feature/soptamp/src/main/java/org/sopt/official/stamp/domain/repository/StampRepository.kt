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
package org.sopt.official.stamp.domain.repository

import org.sopt.official.stamp.domain.model.Archive
import org.sopt.stamp.feature.mission.model.ImageModel

interface StampRepository {
    suspend fun completeMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ): Result<Unit>

    suspend fun getMissionContent(
        missionId: Int,
        nickname: String,
    ): Result<Archive>

    suspend fun modifyMission(
        missionId: Int,
        imageUri: ImageModel,
        content: String
    ): Result<Unit>

    suspend fun deleteMission(missionId: Int): Result<Unit>

    suspend fun deleteAllStamps(): Result<Unit>
}

/*
 * SPDX-FileCopyrightText: 2024 The Calyx Institute
 * SPDX-License-Identifier: Apache-2.0
 */

package com.stevesoltys.seedvault

import android.os.Build.VERSION.SDK_INT
import android.provider.Settings.Secure.ANDROID_ID
import android.util.Log
import com.google.protobuf.ByteString
import com.stevesoltys.seedvault.metadata.BackupMetadata
import com.stevesoltys.seedvault.metadata.BackupType
import com.stevesoltys.seedvault.proto.Snapshot
import com.stevesoltys.seedvault.proto.Snapshot.BackupType.FULL
import com.stevesoltys.seedvault.proto.Snapshot.BackupType.KV
import com.stevesoltys.seedvault.proto.SnapshotKt.apk
import com.stevesoltys.seedvault.proto.SnapshotKt.app
import com.stevesoltys.seedvault.proto.SnapshotKt.split
import com.stevesoltys.seedvault.proto.snapshot
import java.util.Base64
import kotlin.random.Random

object SnapshotWriter {

    fun createSnapshot(metadata: BackupMetadata): Snapshot = snapshot {
        token = metadata.token
        name = metadata.deviceName
        androidId = ANDROID_ID
        sdkInt = SDK_INT
        androidIncremental = metadata.androidIncremental
        d2D = metadata.d2dBackup
        Log.e("TEST", "numApps: ${metadata.packageMetadataMap.size}")
        metadata.packageMetadataMap.forEach { (packageName, m) ->
            apps[packageName] = app {
                time = m.time
                state = m.state.name
                type = if (m.backupType == BackupType.KV) KV else FULL
                name = m.name?.toString() ?: ""
                system = m.system
                launchableSystemApp = m.isLaunchableSystemApp
                blobs += listOf(
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                    ByteString.copyFrom(Random.nextBytes(32)),
                )
                apk = apk {
                    versionCode = m.version ?: 0
                    installer = m.installer ?: ""
                    signatures += m.signatures?.map {
                        ByteString.copyFrom(Base64.getDecoder().decode(it))
                    } ?: emptyList()
                    splits += m.splits?.map {
                        split {
                            name = it.name
                            blobs += listOf(
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                                ByteString.copyFrom(Random.nextBytes(32)),
                            )
                        }
                    } ?: emptyList()
                }
            }
            iconBlobs += listOf(
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
                ByteString.copyFrom(Random.nextBytes(32)),
            )
        }
    }

}

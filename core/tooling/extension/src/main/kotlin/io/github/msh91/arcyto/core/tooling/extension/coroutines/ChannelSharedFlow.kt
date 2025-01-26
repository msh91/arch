package io.github.msh91.arcyto.core.tooling.extension.coroutines
/*
 * Copyright 2022 Daniele Segato
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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

// source: https://gist.github.com/danielesegato/160fabdcba5f563f1a171012377ea041

/**
 * A SharedFlow backed by a channel that hold onto elements until there's at least 1 subscriber
 * to consume them.
 */
@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface ChannelSharedFlow<T> : SharedFlow<T>, FlowCollector<T>

fun <T> ChannelSharedFlow(
    scope: CoroutineScope,
    replay: Int = 0,
    bufferCapacity: Int = Channel.BUFFERED,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    onUndeliveredElement: ((T) -> Unit)? = null,
): ChannelSharedFlow<T> = ChannelSharedFlowImpl(
    scope = scope,
    replay = replay,
    bufferCapacity = bufferCapacity,
    onBufferOverflow = onBufferOverflow,
    onUndeliveredElement = onUndeliveredElement,
)

private class ChannelSharedFlowImpl<T>(
    scope: CoroutineScope,
    replay: Int = 0,
    bufferCapacity: Int = Channel.BUFFERED,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    onUndeliveredElement: ((T) -> Unit)? = null,
) : ChannelSharedFlow<T> {

    private val channel = Channel(
        capacity = bufferCapacity,
        onBufferOverflow = onBufferOverflow,
        onUndeliveredElement = onUndeliveredElement,
    )
    private val shared = channel
        .receiveAsFlow()
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = replay,
        )

    init {
        scope.launch {
            try {
                awaitCancellation()
            } finally {
                channel.close()
            }
        }
    }

    override suspend fun emit(value: T) {
        channel.send(value)
    }

    override val replayCache: List<T> = shared.replayCache

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        shared.collect(collector)
    }
}
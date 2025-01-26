package io.github.msh91.arcyto.core.tooling.extension.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel

fun <T> CoroutineScope.eventsFlow(
    replay: Int = 0,
    bufferCapacity: Int = Channel.BUFFERED,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
    onUndeliveredElement: ((T) -> Unit)? = null,
) = ChannelSharedFlow(this, replay, bufferCapacity, onBufferOverflow, onUndeliveredElement)
package com.arkivanov.mvikotlin.timetravel.proto.internal.io

import com.arkivanov.mvikotlin.timetravel.proto.internal.data.ProtoObject
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelcomand.TimeTravelCommand
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelcomand.writeTimeTravelCommand
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelstateupdate.TimeTravelStateUpdate
import com.arkivanov.mvikotlin.timetravel.proto.internal.data.timetravelstateupdate.writeTimeTravelStateUpdate
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoObjectType.TIME_TRAVEL_COMMAND
import com.arkivanov.mvikotlin.timetravel.proto.internal.io.ProtoObjectType.TIME_TRAVEL_STATE_UPDATE

class ProtoEncoder(
    private val consume: (data: ByteArray, size: Int) -> Unit
) {

    private val writer = DataWriter()

    fun encode(obj: ProtoObject) {
        writer.reset()
        writer.encode(obj)
        consume(writer.data, writer.size)
    }

    private fun DataWriter.encode(obj: ProtoObject) {
        when (obj) {
            is TimeTravelStateUpdate -> writeTyped(TIME_TRAVEL_STATE_UPDATE) { writeTimeTravelStateUpdate(obj) }
            is TimeTravelCommand -> writeTyped(TIME_TRAVEL_COMMAND) { writeTimeTravelCommand(obj) }
            else -> throw IllegalArgumentException("Unsupported proto object type: $this")
        }
    }

    private inline fun writeTyped(type: ProtoObjectType, block: DataWriter.() -> Unit) {
        writer.writeEnum(type)
        writer.block()
    }
}

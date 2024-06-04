package com.example.calendarbyourselvesdacs3.data.repository.event

import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import java.time.LocalDate

const val EVENTS_COLLECTION_REF = "events"

class EventRepository {
    private val eventsRef: CollectionReference =
        Firebase.firestore.collection(EVENTS_COLLECTION_REF)

    /**
     * .addOnSuccessListener { ... }: Đây là một hàm callback được gọi khi thao tác lấy dữ liệu từ Firestore thành công.
     *
     * it?.toObject(Event::class.java): it là tài liệu nhận được từ Firestore. toObject(Event::class.java) chuyển đổi tài liệu này thành một đối tượng Event.
     *
     * onSuccess.invoke(...): Gọi hàm onSuccess với đối tượng Event được chuyển đổi. Nếu tài liệu không tồn tại, it sẽ là null và toObject sẽ trả về null.
     * */
    suspend fun getEvent(
        eventId: String,
        onError: (Throwable?) -> Unit,
        onGetEvent: (Event?) -> Unit
    ) {
        eventsRef
            .document(eventId)
            .get()
            .addOnSuccessListener {
                onGetEvent.invoke(it?.toObject(Event::class.java))
            }
            .addOnFailureListener {
                onError.invoke(it.cause)
            }
    }


    /**
     * val documentId = eventsRef.document().id: Tạo một ID ngẫu nhiên mới cho tài liệu (sự kiện) trong Firestore.
     * eventsRef.document() tạo một tham chiếu đến tài liệu mới, và .id lấy ID của tài liệu đó.
     *
     * eventsRef.document(documentId).set(event): Truy cập tài liệu với ID vừa tạo và thiết lập dữ liệu của tài liệu đó bằng đối tượng event.
     *
     * */
    fun addEvent(event: Event, onComplete: (Boolean) -> Unit) {
        val documentId = eventsRef.document().id

        eventsRef
            .document(documentId)
            .set(event)
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }


    suspend fun deleteEvent(eventId: String, onComplete: (Boolean) -> Unit) {
        eventsRef
            .document(eventId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }


    /**
     * val updateData = hashMapOf<String, Any>(...): Tạo một HashMap chứa các cặp khóa-giá trị đại diện cho các thuộc tính của sự kiện cần cập nhật.
     * "title" to title: Cặp khóa-giá trị để cập nhật thuộc tính title.
     * "description" to description: Cặp khóa-giá trị để cập nhật thuộc tính description.
     * "colorIndex" to colorIndex: Cặp khóa-giá trị để cập nhật thuộc tính colorIndex.
     * */
    suspend fun updateEvent(event: Event, onComplete: (Boolean) -> Unit) {
        val updateData = hashMapOf<String, Any>(
            "title" to event.title,
            "description" to event.description,
            "isCheckAllDay" to event.isCheckAllDay,
            "isCheckNotification" to event.isCheckNotification,
            "startDay" to event.startDay,
            "endDay" to event.endDay,
            "colorIndex" to event.colorIndex
        )

        eventsRef
            .document(event.documentId)
            .update(updateData)
            .addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }

    suspend fun loadEventByDate(date: LocalDate, onAllEvents: (List<Event>) -> Unit) {
        eventsRef
            .whereEqualTo("startDay", date.toString())
            .get()
            .addOnSuccessListener {
                val listEvent = arrayListOf<Event>()
                for (e in it) {
                    val event = e.toObject(Event::class.java)
                    listEvent.add(event)
                }

                onAllEvents.invoke(listEvent)
            }
    }

    suspend fun loadEventBySearch(queryValue: String, onAllEvents: (List<Event>) -> Unit) {
        eventsRef
            .whereEqualTo("startDay", queryValue)
            .whereEqualTo("title", queryValue)
            .whereEqualTo("description", queryValue)
            .get()
            .addOnSuccessListener {
                val listEvent = arrayListOf<Event>()
                for (e in it) {
                    val event = e.toObject(Event::class.java)
                    listEvent.add(event)
                }

                onAllEvents.invoke(listEvent)
            }
    }
}
package com.example.calendarbyourselvesdacs3.data.repository.event

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.example.calendarbyourselvesdacs3.domain.model.event.localDateToString
import com.example.calendarbyourselvesdacs3.domain.model.event.timestampToString
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate


const val EVENTS_COLLECTION_REF = "events"

class EventRepository{
    private val eventsRef: CollectionReference =
        Firebase.firestore.collection(EVENTS_COLLECTION_REF)
    fun user() = Firebase.auth.currentUser
    /**
     * .addOnSuccessListener { ... }: Đây là một hàm callback được gọi khi thao tác lấy dữ liệu từ Firestore thành công.
     *
     * it?.toObject(Event::class.java): it là tài liệu nhận được từ Firestore. toObject(Event::class.java) chuyển đổi tài liệu này thành một đối tượng Event.
     *
     * onSuccess.invoke(...): Gọi hàm onSuccess với đối tượng Event được chuyển đổi. Nếu tài liệu không tồn tại, it sẽ là null và toObject sẽ trả về null.
     * */
     fun getEvent(
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

        var newEvent = Event(
            userId = event.userId,
            title = event.title,
            description = event.description,
            checkAllDay = event.checkAllDay,
            checkNotification = event.checkNotification,
            startDay = event.startDay,
            startDate = timestampToString(event.startDay),
            endDay = event.endDay,
            colorIndex = event.colorIndex,
            documentId = documentId,
        )

        eventsRef
            .document(documentId)
            .set(newEvent)
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }


     fun deleteEvent(eventId: String, onComplete: (Boolean) -> Unit) {
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
     fun updateEvent(event: Event, onComplete: (Boolean) -> Unit) {
        val updateData = hashMapOf<String, Any>(
            "title" to event.title,
            "description" to event.description,
            "checkAllDay" to event.checkAllDay,
            "checkNotification" to event.checkNotification,
            "startDay" to event.startDay,
            "startDate" to timestampToString(event.startDay),
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



    fun loadEventByDate(userId: String, date: LocalDate): Flow<Resource<List<Event>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = eventsRef
                .orderBy("startDay")
                .whereEqualTo("userId", userId)
                .whereEqualTo("startDate", localDateToString(date))
                .addSnapshotListener{ snapshot, e ->
                    val response = if (snapshot != null) {
                        val events = snapshot.toObjects(Event::class.java)
                        Resource.Success(data = events)
                    } else {
                        Resource.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        }catch (e: Exception){
            trySend(Resource.Error(e?.cause))
            e.printStackTrace()
        }
        /**       Đảm bảo rằng khi Flow đóng, listener sẽ được gỡ bỏ để tránh rò rỉ bộ nhớ. */
        awaitClose {
            snapshotStateListener?.remove()
        }
    }



    fun loadEventBySearch(userId: String, queryValue: String): Flow<Resource<List<Event>>> = callbackFlow {
        var titleSnapshotStateListener: ListenerRegistration? = null

        try {
            titleSnapshotStateListener = eventsRef
                .whereEqualTo("userId", userId)
////                .whereGreaterThanOrEqualTo("title", queryValue)
//                .whereLessThanOrEqualTo("title", queryValue + "\uf8ff")
                .orderBy("startDay")
                .addSnapshotListener{ snapshot, e ->
                    val response = if (snapshot != null) {
                        val events = snapshot.documents
                            .filter { document ->
                                val title = document.getString("title")?.toLowerCase()
                                title?.contains(queryValue.toLowerCase())?: false
                            }
                            .mapNotNull { document ->
                                document.toObject(Event::class.java)
                            }
                        Resource.Success(data = events)
                    } else {
                        Resource.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        }catch (e: Exception){
            trySend(Resource.Error(e?.cause))
            e.printStackTrace()
        }
        /**       Đảm bảo rằng khi Flow đóng, listener sẽ được gỡ bỏ để tránh rò rỉ bộ nhớ. */
        awaitClose {
            titleSnapshotStateListener?.remove()
        }
    }
}
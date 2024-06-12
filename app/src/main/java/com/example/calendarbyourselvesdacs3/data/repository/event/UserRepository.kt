package com.example.calendarbyourselvesdacs3.data.repository.event

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.user.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val USERS_COLLECTION_REF = "users"

class UserRepository {
    private val usersRef: CollectionReference =
        Firebase.firestore.collection(USERS_COLLECTION_REF)

    fun loadUserBySearch(queryValue: String): Flow<Resource<List<User>>> =
        callbackFlow {
            var titleSnapshotStateListener: ListenerRegistration? = null

            try {
                titleSnapshotStateListener = usersRef
                    .orderBy("email")
                    .addSnapshotListener { snapshot, e ->
                        val response = if (snapshot != null) {
                            val users = snapshot.documents
                                .filter { document ->
                                    val email = document.getString("email")?.toLowerCase()
                                    email?.contains(queryValue.toLowerCase()) ?: false
                                }
                                .mapNotNull { document ->
                                    document.toObject(User::class.java)
                                }
                            Resource.Success(data = users)
                        } else {
                            Resource.Error(throwable = e?.cause)
                        }
                        trySend(response)
                    }
            } catch (e: Exception) {
                trySend(Resource.Error(e?.cause))
                e.printStackTrace()
            }
            /**       Đảm bảo rằng khi Flow đóng, listener sẽ được gỡ bỏ để tránh rò rỉ bộ nhớ. */
            awaitClose {
                titleSnapshotStateListener?.remove()
            }
        }


    fun addUser(user: User, onComplete: (Boolean) -> Unit) {
        val dcmId = user.uid
        usersRef
            .document(dcmId)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    onComplete(false)
                } else {
                    var newUser = User(
                        uid = user.uid,
                        email = user.email,
                        username = user.username
                    )
                    usersRef
                        .document(dcmId)
                        .set(newUser)
                        .addOnCompleteListener {
                            onComplete.invoke(it.isSuccessful)
                        }
                }
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }

    fun getUser(
        email: String,
        onError: (Throwable?) -> Unit,
        onGetUser: (User?) -> Unit
    ) {
        usersRef
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    val user = snapshot.documents.firstOrNull()?.toObject(User::class.java)
                    onGetUser(user)
                } else {
                    onGetUser(null)  // Không tìm thấy user
                }
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }
}
## Documentação do Firebase com email e senha
https://firebase.google.com/docs/auth/android/password-auth?hl=pt-br

### Exemplo de autenticação com email e senha:

```
import com.google.firebase.auth.FirebaseAuth
val auth: FirebaseAuth = FirebaseAuth.getInstance()

fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.localizedMessage)
            }
        }
}

fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.localizedMessage)
            }
        }
}
```

### Exemplo para poder salvar e buscar os dados de lá:
```
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()

fun saveUserData(userId: String, userName: String) {
    val user = hashMapOf(
        "name" to userName
    )
    db.collection("users").document(userId)
        .set(user)
        .addOnSuccessListener {
            println("User data saved!")
        }
        .addOnFailureListener { e ->
            println("Error adding document $e")
        }
}

fun getUserData(userId: String, onDataRetrieved: (String?) -> Unit) {
    db.collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val name = document.getString("name")
                onDataRetrieved(name)
            } else {
                onDataRetrieved(null)
            }
        }
}
```

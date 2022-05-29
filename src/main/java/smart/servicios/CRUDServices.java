package smart.servicios;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import smart.models.Usuario;

@Service
public class CRUDServices{
	
	private Firestore db;
	
	public CRUDServices() {
	}
	
	public String create(Object obj, String id) throws InterruptedException, ExecutionException {
		db=FirestoreClient.getFirestore();
		String coleccion = obj.getClass().getSimpleName().toLowerCase()+"s";
		ApiFuture<WriteResult> result = db.collection(coleccion).document(id).set(obj);
		return id+":"+result.get().getUpdateTime().toString();
	}


	public Object read(String id, Class<?> clase) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		String coleccion = clase.getSimpleName().toLowerCase()+"s";
		DocumentReference docRef = db.collection(coleccion).document(id);
		ApiFuture<DocumentSnapshot> af = docRef.get();
		DocumentSnapshot doc = af.get();
		Object obj = null;
		if(doc.exists()) {
			obj = doc.toObject(clase);
			return obj;
		}else {
			return null;
		}
	}


	public String update(Object obj,String id) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		String coleccion = obj.getClass().getSimpleName().toLowerCase()+"s";
		ApiFuture<WriteResult> result = db.collection(coleccion).document(id).set(obj);
		return id+":"+result.get().getUpdateTime().toString();
	}

	public String delete(String id, Class<?> clase) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		String coleccion = clase.getSimpleName().toLowerCase()+"s";
		ApiFuture<WriteResult> result = db.collection(coleccion).document(id).delete();
		return id+":"+result.get().getUpdateTime().toString();
	}
	 public LinkedList<Object> getAllDocs(Class<?> clase) throws InterruptedException, ExecutionException {
		 db = FirestoreClient.getFirestore();
		String coleccion = clase.getSimpleName().toLowerCase()+"s";
		Iterable<DocumentReference> documentos = db.collection(coleccion).listDocuments();
		LinkedList<Object> objetos = new LinkedList<>();
		for (DocumentReference documentReference : documentos) {
			objetos.add(documentReference.get().get().toObject(clase));
		}
		return objetos;
	}
	 public DocumentReference getDocRef(String coleccion, String id) {
		 db=FirestoreClient.getFirestore();
		 return db.collection(coleccion).document(id);
	 }
	 public DocumentReference newDoc(String coleccion) {
		 db=FirestoreClient.getFirestore();
		 return db.collection(coleccion).document();
	 }
	 public Object readFromRef(DocumentReference docRef, Class<?> clase) throws InterruptedException, ExecutionException {
			ApiFuture<DocumentSnapshot> af = docRef.get();
			DocumentSnapshot doc = af.get();
			Object obj = null;
			if(doc.exists()) {
				obj = doc.toObject(clase);
				return obj;
			}else {
				return null;
			}
	}
}

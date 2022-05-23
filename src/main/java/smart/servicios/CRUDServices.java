package smart.servicios;

import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import smart.models.CollectFireStore;
import smart.models.Usuario;

public abstract class CRUDServices{
	
	protected String coleccion;
	
	
	public String create(CollectFireStore obj) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> caf = db.collection(coleccion).document(obj.getId()).set(obj);
		return caf.get().getUpdateTime().toString();
	}


	public Object read(String id, Class<?> clase) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection(coleccion).document(id);
		ApiFuture<DocumentSnapshot> af = docRef.get();
		DocumentSnapshot doc = af.get();
		Object obj = null;
		if(doc.exists()) {
			obj = doc.toObject(clase);
			return obj;
		}else return null;
	}


	public String update(CollectFireStore obj) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> caf = db.collection(coleccion).document(obj.getId()).set(obj);
		return caf.get().getUpdateTime().toString();
	}

	public String delete(String id) throws InterruptedException, ExecutionException {
		Firestore db = FirestoreClient.getFirestore();
		ApiFuture<WriteResult> caf = db.collection(coleccion).document(id).delete();
		return "Documento con id : " + id + " eliminado al tiempo: " + caf.get().getUpdateTime().toString();
	}
}

package smart.servicios;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import smart.models.Equipo;
import smart.models.Registro;
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
		String dateFormat = (new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss")).format(result.get().getUpdateTime().toDate());
		return id+"/"+dateFormat;
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
		String dateFormat = (new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss")).format(result.get().getUpdateTime().toDate());
		return id+"/"+dateFormat;
	}

	public String delete(String id, Class<?> clase) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		String coleccion = clase.getSimpleName().toLowerCase()+"s";
		ApiFuture<WriteResult> result = db.collection(coleccion).document(id).delete();
		String dateFormat = (new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss")).format(result.get().getUpdateTime().toDate());
		return id+"/"+dateFormat;
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
	 public LinkedList<DocumentReference> getUserDocRef() throws InterruptedException, ExecutionException {
		 db = FirestoreClient.getFirestore();
		Iterable<DocumentReference> documentos = db.collection("usuarios").listDocuments();
		LinkedList<DocumentReference> objetos = new LinkedList<>();
		for (DocumentReference documentReference : documentos) {
			objetos.add(documentReference);
		}
		return objetos;
	}
	 public LinkedList<Usuario> getAllUsersSede(DocumentReference sede) throws InterruptedException, ExecutionException {
		 db = FirestoreClient.getFirestore();
		 List<QueryDocumentSnapshot> documentos = db.collection("usuarios").whereEqualTo("sede", sede).get().get().getDocuments();
		 LinkedList<Usuario> usuarios = new LinkedList<>();
		 for(DocumentSnapshot doc : documentos) {
			 usuarios.add(doc.toObject(Usuario.class));
		 }
		 return usuarios;
	}
	 public LinkedList<Registro> getRegistrosWhere(DocumentReference sede,DocumentReference user,String desde , String hasta) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		List<QueryDocumentSnapshot> documentos = db.collection("registros")
				.whereEqualTo("sede", sede)
				.whereEqualTo("usuario",user)
				.whereGreaterThan("fecha",Timestamp.valueOf(desde+" 00:00:00"))
				.whereLessThan("fecha",Timestamp.valueOf(hasta+" 23:59:59"))
				.get().get().getDocuments();
		LinkedList<Registro> registros = new LinkedList<>();
		for(DocumentSnapshot reg: documentos) {
				registros.add(reg.toObject(Registro.class));
		}
		 return registros;
	}

	 public LinkedList<Equipo> getEquiposWhere(DocumentReference sala) throws InterruptedException, ExecutionException {
		db = FirestoreClient.getFirestore();
		List<QueryDocumentSnapshot> documentos = db.collection("equipos")
				.whereEqualTo("sala", sala)
				.get().get().getDocuments();
		LinkedList<Equipo> equipos = new LinkedList<>();
		for(DocumentSnapshot eq: documentos) {
				equipos.add(eq.toObject(Equipo.class));
		}
		 return equipos;
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

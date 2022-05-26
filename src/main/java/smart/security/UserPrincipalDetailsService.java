package smart.security;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import smart.models.Usuario;

public class UserPrincipalDetailsService implements UserDetailsService{
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		String[] datos = username.split(":");
		Firestore db = FirestoreClient.getFirestore();
		DocumentReference docRef = db.collection("usuarios").document(datos[0]);
		ApiFuture<DocumentSnapshot> af = docRef.get();
		DocumentSnapshot doc=null;
		try {
			doc = af.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		Usuario user = null;
		if(doc.exists()) {
			user = doc.toObject(Usuario.class);
			if(user.getNombre().equals(datos[1].toLowerCase()) && user.getApellido().equals(datos[2].toLowerCase())) {
				UserPrincipal userPrince = new UserPrincipal(user);
				return userPrince;
			}else {
				return null;
			}
		}else {
			return null;
		}
	}

}

package smart.servicios;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FireBaseIniciador {
	
	@PostConstruct
	public void iniciador(){
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream("./serviceAccountKey.json");
			FirebaseOptions options;
			try {
				options = new FirebaseOptions.Builder()
						  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
						  .setDatabaseUrl("https://smartapp-3f7fc-default-rtdb.firebaseio.com")
						  .build();

				FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Encontre archivo pero error en set credentials");
			}
		} catch (FileNotFoundException e) {
			System.out.println("No Encontre Archivo");
			e.printStackTrace();
		}
	}
}

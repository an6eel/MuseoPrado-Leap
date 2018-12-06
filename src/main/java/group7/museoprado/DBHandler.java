package group7.museoprado;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.jsoup.Jsoup;

public class DBHandler {

    private Firestore db;
    private List<QueryDocumentSnapshot> artists;
    private List<QueryDocumentSnapshot> paintings;
    private static QueryDocumentSnapshot ARTIST;
    private int INDEX_ART;
    private int INDEX_PAINT;

    public DBHandler() throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount = new FileInputStream("lib/key.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://name-5b700.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();

        CollectionReference x = db.collection("Artistas");
        artists = db.collection("Artistas").get().get().getDocuments();
        ARTIST = artists.get(INDEX_ART);

        Query y =db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        paintings = y.get().get().getDocuments();


    }


    public String getInfoPaint(){
        String info = "";
        DocumentSnapshot paint = paintings.get(INDEX_PAINT);

        ArrayList<String> name = (ArrayList<String>) paint.get("name");
        info+=("<b>Nombre:</b> "+ name.get(0));
        if(name.size()>1){

            for(int i=1;i<name.size()-1;++i)
                info+=(", " + name.get(i));
            info+=(" o " + name.get(name.size()-1));
        }
        info+=("<br/>");
        info+=("<b> Autor: </b> "+ paint.get("autor")+ ", " + paint.get("date")+ "<br/>");
        if(paint.contains("collection"))
            info+=("<b> Coleccion: </b> "+ paint.get("collection") + "<br/>");
        if(paint.contains("sala"))
            info+=("<b> Sala: </b> "+ paint.get("sala") + "<br/>");
        info+=("<b> Resumen: </b> "+ paint.get("resumen") + "<br/>");
        return  info;
    }

    public void nextAuthor(){
        // TODO
    }

    public void previousAuthor(){
        // TODO
    }

    public void nextPainting(){
        // TODO
    }

    public void previousPainting(){
        // TODO
    }

}

package group7.museoprado;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;


import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import org.jsoup.Jsoup;

public class DBHandler {

    private Firestore db;
    private List<QueryDocumentSnapshot> artists;
    private List<QueryDocumentSnapshot> paintings;
    private List<QueryDocumentSnapshot> all_paintings;
    private static QueryDocumentSnapshot ARTIST;
    private int INDEX_ART;
    private int INDEX_PAINT;
    private int INDEX_PAINT_ALL;

    public DBHandler() throws IOException, ExecutionException, InterruptedException {
        FileInputStream serviceAccount = new FileInputStream("lib/key.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://name-5b700.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();

        INDEX_ART=INDEX_PAINT=INDEX_PAINT_ALL=0;

        CollectionReference x = db.collection("Artistas");
        artists = db.collection("Artistas").get().get().getDocuments();
        ARTIST = artists.get(INDEX_ART);

        Query y = db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        all_paintings = db.collection("obras").get().get().getDocuments();
        paintings = y.get().get().getDocuments();


    }


    public String getInfoPaint(){
        String info = "";
        DocumentSnapshot paint = paintings.get(INDEX_PAINT);

        ArrayList<String> name = (ArrayList<String>) paint.get("name");
        info+=("Nombre:\n\t"+ name.get(0));
        if(name.size()>1){

            for(int i=1;i<name.size()-1;++i)
                info+=(", " + name.get(i));
            info+=(" o " + name.get(name.size()-1));
        }
        info+=("\n");
        info+=(" Autor:\n\t"+ paint.get("autor")+ ", " + paint.get("date")+ "\n");
        if(paint.contains("collection"))
            info+=("Coleccion:\n\t"+ paint.get("collection") + "\n");
        if(paint.contains("sala"))
            info+=("Sala:\n\t"+ paint.get("sala") + "\n");
        info+=(" Resumen:\n\t"+ paint.get("info") + "");
        return  info;
    }

    public String getInfoArtist(){
        String info = "";
        info+=("Nombre:\n\t"+ ARTIST.get("name")+"\n");
        Date birth = ARTIST.getDate("birthdate");
        Date death = ARTIST.getDate("deathdate");
        SimpleDateFormat ft =
                new SimpleDateFormat ("dd/MMMM/yyyy");
        info+=("Fecha de Nacimiento:\n\t"+ ft.format(birth)+ "\n");
        info+=("Lugar de Nacimiento: \n\t"+ ARTIST.get("birthplace")+"\n");
        info+=("Fecha de Fallecimiento:\n\t"+ ft.format(death)+ "\n");
        if(ARTIST.contains("etapas")) {
            Map<String, String> etapas = (Map) ARTIST.get("etapas");
            info += ("Etapas:\n");
            for (String key : etapas.keySet())
                info += ( key + ": " + etapas.get(key) + "\n");
        }
        ArrayList<String> influencias = (ArrayList<String>) ARTIST.get("influencias");
        info+=("nfluencias: \n\t"+ influencias.get(0));
        for(int i=1;i<influencias.size();++i)
            info+=(","+influencias.get(i));
        info+=("\n");

        ArrayList<String> life = (ArrayList<String>) ARTIST.get("lifeplace");
        info+=("Donde vivio: \n\t"+ life.get(0));
        for(int i=1;i<life.size();++i)
            info+=(","+ life.get(i));
        info+=("\n\nResumen:\n\t"+ARTIST.get("info"));
        return info;
    }


    public QueryDocumentSnapshot getFirstPaintingAll(){
        INDEX_PAINT_ALL = 0;
        return all_paintings.get(INDEX_PAINT_ALL);
    }

    public QueryDocumentSnapshot nextPaintingAll(){
        INDEX_PAINT_ALL++;
        if(INDEX_PAINT_ALL== all_paintings.size())
            INDEX_PAINT_ALL=0;
        return  all_paintings.get(INDEX_PAINT_ALL);
    }

    public QueryDocumentSnapshot previousPaintingAll(){
        if(INDEX_PAINT_ALL == 0)
            INDEX_PAINT_ALL = all_paintings.size()-1;
        else
            INDEX_PAINT_ALL--;
        return  all_paintings.get(INDEX_PAINT_ALL);
    }

    public QueryDocumentSnapshot getFirstArtist() throws ExecutionException, InterruptedException {
        INDEX_ART=0;
        ARTIST = artists.get(INDEX_ART);
        Query y = db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        paintings = y.get().get().getDocuments();
        return ARTIST;
    }

    public QueryDocumentSnapshot nextArtist() throws ExecutionException, InterruptedException {
        INDEX_ART++;
        if(INDEX_ART==artists.size())
            INDEX_ART=0;
        ARTIST = artists.get(INDEX_ART);
        Query y = db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        paintings = y.get().get().getDocuments();
        return ARTIST;
    }

    public QueryDocumentSnapshot previousArtist() throws ExecutionException, InterruptedException {
        if(INDEX_ART==0)
            INDEX_ART=artists.size()-1;
        else
            INDEX_ART--;
        ARTIST = artists.get(INDEX_ART);
        Query y = db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        paintings = y.get().get().getDocuments();
        return ARTIST;
    }

    public QueryDocumentSnapshot getFirstPaint(){
        INDEX_PAINT=0;
        return paintings.get(INDEX_PAINT);
    }

    public QueryDocumentSnapshot getNextPaint(){
        INDEX_PAINT++;
        if(INDEX_PAINT==paintings.size())
            INDEX_PAINT=0;
        return paintings.get(INDEX_PAINT);
    }

    public QueryDocumentSnapshot getPreviousArtist(){
        if(INDEX_PAINT==0)
            INDEX_PAINT=paintings.size()-1;
        else
            INDEX_PAINT--;
        return paintings.get(INDEX_PAINT);
    }

    public QueryDocumentSnapshot getArtistOfAll() throws ExecutionException, InterruptedException {
        String name = (String) all_paintings.get(INDEX_PAINT_ALL).get("autor");
        INDEX_ART=0;
        while(name.equalsIgnoreCase(artists.get(INDEX_ART).getId())){
            INDEX_ART++;
        }

        Query y = db.collection("obras").whereEqualTo("autor",ARTIST.getId());
        paintings = y.get().get().getDocuments();
        return artists.get(INDEX_ART);
    }

    public QueryDocumentSnapshot getActualArtist(){
        return artists.get(INDEX_ART);
    }
}

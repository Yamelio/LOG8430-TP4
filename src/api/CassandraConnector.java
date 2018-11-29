package api;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Map;

public class CassandraConnector {


    private static Cluster cluster;
    private static Session session;

    public static void connect(){
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("log8430");
    }

    public static String getFactures(){
        ResultSet rs = session.execute("SELECT json * from facture");
        return toJson(rs);
    }

    public static String getProduits(){
        ResultSet rs = session.execute("SELECT json * from produit");
        return toJson(rs);
    }

    public static void addFacture(int fid, Map<Integer,Integer> items){
        for(int i : items.keySet()){
            session.execute("insert into facture(fid,pid,qte) values("+fid+","+i+","+items.get(i));
        }
    }



    private static String toJson(ResultSet rs){
        int i = 0;
        String res = "[";
        for (Row row : rs) {
            if (i > 0)
                res += ",";
            i++;
            String json = row.getString(0);
            res += json;
        }
        res += "]";
        return res;
    }

}

package api;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.mllib.fpm.AssociationRules;
import org.apache.spark.mllib.fpm.FPGrowth;
import org.apache.spark.mllib.fpm.FPGrowthModel;

import java.util.Map;

public class SparkConnector {

    public static void connect(){
        JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

        return SparkSession spark = SparkSession.builder()
            .master(192.168.111.20)
            .appName('log8430')
            .getOrCreate();
    }

    /**
     * le but est ici de construire un tableau [1223,4445,124,,...] regroupant les achats par facture
     * @param rs
     */
    public static void parseResultDB(ResultSet rs){

        String data[] = {};
        for (Row row : rs){
            if(row['fid'] && row['pid']){
                while (row['qte'] != 0 || row['qte'])
                    data[int(row['fid'])] += row ['pid'];
                    row['pid']--;
            }
        }
        return data;
    }

    public static void getFrequentProducts (){

        ResultSet rawData = toJson(CassandraConnector.getFactures());

        JavaRDD<List<String>> data = parseResultDB(rawData);

        FPGrowth fpg = new FPGrowth()
                .setMinSupport(0.2)
                .setNumPartitions(2);
        FPGrowthModel<String> model = fpg.run(data);

        String result = "";

        for (FPGrowth.FreqItemset<String> itemset: model.freqItemsets().toJavaRDD().collect()) {
            result +="[" + itemset.javaItems() + "], " + itemset.freq());
        }
        return result;
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

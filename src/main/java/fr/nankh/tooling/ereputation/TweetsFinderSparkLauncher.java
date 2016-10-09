package fr.nankh.tooling.ereputation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import twitter4j.Status;
import twitter4j.auth.Authorization;
import twitter4j.auth.AuthorizationFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;


public class TweetsFinderSparkLauncher {



    public static void main(String[] args) throws InterruptedException {

        //Récupération de la configuration
        fr.nankh.tooling.ereputation.Configuration confApp = fr.nankh.tooling.ereputation.Configuration.getInstance();

        // Jackson Mapper
        ObjectMapper mapper = new ObjectMapper();

        //Conf Twitter
        Configuration twitterConf = ConfigurationContext.getInstance();
        Authorization twitterAuth = AuthorizationFactory.getInstance(twitterConf);

        //Conf Spark + ElasticSearch
        SparkConf sparkConf = new SparkConf().setAppName(confApp.getSparkAppName()).setMaster(confApp.getSparkMaster())
                .set("spark.serializer", confApp.getSparkSerializer())
                .set("es.nodes", confApp.getElasticSearchNodes())
                .set("es.index.auto.create", confApp.getElasticSearchIndexAutoCreate());
        //Conf de Streaming (
        JavaStreamingContext sc = new JavaStreamingContext(sparkConf, new Duration(confApp.getDurationStreaming()));

        String[] filters = confApp.getTwitterFilters();

        JavaReceiverInputDStream<twitter4j.Status> twitterStream = TwitterUtils.createStream(sc, twitterAuth, filters);


        JavaDStream<String> statuses = twitterStream
                .map(new Function<Status, String>() {
                    @Override
                    public String call(Status status) throws Exception {
                        String result = mapper.writeValueAsString(status);
                        System.err.println(result);
                        return result;
                    }
                });

        statuses.foreachRDD(new VoidFunction2<JavaRDD<String>, Time>() {
            @Override
            public void call(JavaRDD<String> rdd, Time time) throws Exception {

                JavaEsSpark.saveJsonToEs(rdd, confApp.getElasticSearchTarget());

            }
        });

        sc.start();
        sc.awaitTermination();
    }
}

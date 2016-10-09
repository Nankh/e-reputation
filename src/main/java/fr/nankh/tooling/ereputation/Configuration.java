package fr.nankh.tooling.ereputation;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Classe de configuration du module TweetsFinderSpark
 * Created by Ankh on 20/08/2016.
 */
@Slf4j
@Getter
@ToString
public final class Configuration {

    private static Configuration INSTANCE;
    private static final String BUNDLE_NAME = "ereputation.properties";
    private static final String PROP_DURATION_STREAMING = "streaming.duration";
    private static final String PROP_SPARK_APP_NAME = "spark.app.name";
    private static final String PROP_SPARK_MASTER = "spark.master";
    private static final String PROP_SPARK_SERIALIZER = "spark.serializer";
    private static final String PROP_ES_NODES = "es.nodes";
    private static final String PROP_ES_TARGET = "es.target";
    private static final String PROP_ES_INDEX_AUTO_CREATE = "es.index.auto.create";
    private static final String PROP_TWITTER_FILTERS = "twitter.filters";

    private static final String FILTERS_SEPARATOR = ",";

    private int durationStreaming;
    private String sparkAppName;
    private String sparkMaster;
    private String sparkSerializer;
    private String elasticSearchNodes;
    private String elasticSearchTarget;
    private String elasticSearchIndexAutoCreate;
    private String[] twitterFilters;


    /**
     * Constructeur privé.
     * Permet d'éviter l'instanciation de la classe.
     */
    private Configuration() {
        super();
    }

    static {
        INSTANCE = new Configuration();
        log.info("Searching & loading property file {}", BUNDLE_NAME);
        InputStream input = Configuration.class.getClassLoader().getResourceAsStream(BUNDLE_NAME);
        if (input == null) {
            log.error("Sorry, unable to find {}", BUNDLE_NAME);
        } else {
            Properties props = new Properties();
            try {
                props.load(input);
                INSTANCE.durationStreaming =  Integer.parseInt(props.getProperty(PROP_DURATION_STREAMING));
                INSTANCE.sparkAppName = props.getProperty(PROP_SPARK_APP_NAME);
                INSTANCE.sparkMaster = props.getProperty(PROP_SPARK_MASTER);
                INSTANCE.sparkSerializer = props.getProperty(PROP_SPARK_SERIALIZER);
                INSTANCE.elasticSearchIndexAutoCreate = props.getProperty(PROP_ES_INDEX_AUTO_CREATE);
                INSTANCE.elasticSearchNodes = props.getProperty(PROP_ES_NODES);
                INSTANCE.twitterFilters = props.getProperty(PROP_TWITTER_FILTERS).split(FILTERS_SEPARATOR);
                INSTANCE.elasticSearchTarget = props.getProperty(PROP_ES_TARGET);
                log.info("Configuration loaded... {}", INSTANCE);
            } catch (IOException e) {
                log.error("Exception occurs while loading the stream...", e);
                throw new RuntimeException("An exception occurs while loading the stream file");
            }
        }
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }


}

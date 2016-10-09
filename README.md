# e-reputation
This repository allows Users to set up quickly a configuration to extract tweets with the following technologies :
* Java Syntax (No Scala Syntax)
* Spark (Core, Streaming, Tweeter Streaming)

Tweets are extract by filters (=hashtags), transform as JSON and finally stored in an ElasticSearch Instance.

# Plugins needed
In your favorite IDE, you'll have to add lombok plugin.

# Requirements
First, you will have to create a twitter account (and manage it to allow the api access).

# Configuration
You'll only have to complete the following settings in twitter4j.properties (informations available in Tweeter Screens...)
> oauth.consumerKey  
> oauth.consumerSecret  
> oauth.accessToken  
> oauth.accessTokenSecret      

You can custom the configuration of this app by changing the settings in ereputation.properties. 
 

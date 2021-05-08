#!/bin/bash

openssl req -new -x509 -keyout kafka-dev-ca.key -out kafka-dev-ca.crt -days 9999 -subj '/CN=dev.kafka/OU=Eknarong/O=APHIPHUTTHIKUL/L=DEVELOP/S=Bangkok/C=TH' -passin pass:changeit -passout pass:changeit

keytool -genkey -noprompt  -alias zookeeper1 -dname "CN=zookeeper1.dev.kafka, OU=Eknarong, O=APHIPHUTTHIKUL, L=DEVELOP, S=Bangkok, C=TH" -keystore zookeeper1.keystore.jks  -keyalg RSA -storepass changeit -keypass changeit

keytool -keystore zookeeper1.keystore.jks -alias zookeeper1 -certreq -file zookeeper1.csr -storepass changeit -keypass changeit

openssl x509 -req -CA kafka-dev-ca.crt -CAkey kafka-dev-ca.key -in zookeeper1.csr -out zookeeper1-ca-signed.crt -days 9999 -CAcreateserial -passin pass:changeit

keytool -keystore zookeeper1.keystore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

keytool -keystore zookeeper1.keystore.jks -alias zookeeper1 -import -file zookeeper1-ca-signed.crt -storepass changeit -keypass changeit

keytool -keystore zookeeper1.truststore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

keytool -genkey -noprompt  -alias broker1 -dname "CN=broker1.dev.kafka, OU=Eknarong, O=APHIPHUTTHIKUL, L=DEVELOP, S=Bangkok, C=TH" -keystore kafka.broker1.keystore.jks  -keyalg RSA -storepass changeit -keypass changeit

keytool -keystore kafka.broker1.keystore.jks -alias broker1 -certreq -file kafka.broker1.csr -storepass changeit -keypass changeit

openssl x509 -req -CA kafka-dev-ca.crt -CAkey kafka-dev-ca.key -in kafka.broker1.csr -out kafka.broker1-ca-signed.crt -days 9999 -CAcreateserial -passin pass:changeit

keytool -keystore kafka.broker1.keystore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

keytool -keystore kafka.broker1.keystore.jks -alias broker1 -import -file kafka.broker1-ca-signed.crt -storepass changeit -keypass changeit

keytool -keystore kafka.broker1.truststore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

keytool -genkey -noprompt  -alias kclient -dname "CN=kclient.dev.kafka, OU=Eknarong, O=APHIPHUTTHIKUL, L=DEVELOP, S=Bangkok, C=TH" -keystore kafkaclient.keystore.jks  -keyalg RSA -storepass changeit -keypass changeit

keytool -keystore kafkaclient.keystore.jks -alias kclient -certreq -file kafkaclient.csr -storepass changeit -keypass changeit

openssl x509 -req -CA kafka-dev-ca.crt -CAkey kafka-dev-ca.key -in kafkaclient.csr -out kafkaclient-ca-signed.crt -days 9999 -CAcreateserial -passin pass:changeit

keytool -keystore kafkaclient.keystore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

keytool -keystore kafkaclient.keystore.jks -alias kclient -import -file kafkaclient-ca-signed.crt -storepass changeit -keypass changeit

keytool -keystore kafkaclient.truststore.jks -alias CARoot -import -file kafka-dev-ca.crt -storepass changeit -keypass changeit

echo "changeit" > sslkey_pw
echo "changeit" > keystore_pw
echo "changeit" > truststore_pw

keytool -list -v -keystore zookeeper1.keystore.jks -storepass changeit -keypass changeit
keytool -list -v -keystore kafka.broker1.keystore.jks -storepass changeit -keypass changeit
keytool -list -v -keystore kafkaclient.keystore.jks -storepass changeit -keypass changeit
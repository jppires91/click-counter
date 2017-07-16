#Get jetty
curl "http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.6.v20170531/jetty-distribution-9.4.6.v20170531.zip" -o "jetty.zip"
unzip jetty.zip
mv jetty-distribution-9.4.6.v20170531 jetty
rm jetty.zip
rm -rf jetty/demo-base
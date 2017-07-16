# click-counter
Cilck Counter using Websockets; Increment or decrement a shared long between all web sockets' clients

<h2>Description</h2>
<p>The main target of this project is to deal with real-time information in a multiple clients' browser.</p>
<p>The user can click in a button (increment) which increments a variable. This variable is stored in a webserver.</p>
<p>When he clicks in increment, he triggers an event that sends a message to the websocket server, and that server will send the counter to all connected users.</p>
<p>The counter is distributed store in hazelcast instances. Between the client's browser and the hazelcast instances we have a jetty server running the websocket server which sends and receives messages from all clients.</p>
<p>When a user connects to the website, he receives the counter from the server.</p>

<h2>Instructions</h2>
<h4>1 - Install jetty server, running the script inside bin, install_jetty.sh</h4>
<p>or download it from <a href="http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.6.v20170531/jetty-distribution-9.4.6.v20170531.zip">here</a> install it on bin/jetty</p>

<h4>2 - Compile the project (maven is needed), running the script mk_project.sh located in bin dir</h4>
<p>or navigate to src/backend/ and run mvn install</p>

<h4>3 - Deploy the project running the script deploy_links.sh located in bin dir</h4>
<p>or just copy src/backend/click-counter-client/target/click-counter-client.war to bin/jetty/webapps
<br/>and src/backend/click-counter-hz-server/target/click-counter-hz-server.jar to bin/jar</p>

<h4>4 - Now you can start jetty running the script start_jetty.sh located at bin folder</h4>
<p>or run java -jar start.jar in bin/jetty</p>

<h4>5 - You can also run hazelcast instance, by running start_hz.sh in bin folder. You can run how many you want. They will just form a cluster.
<p>or run java -jar click-counter-hz-server.jar in bin/jar</p>

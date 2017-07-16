mkdir deploy
ln -sf ../../src/backend/click-counter-client/target/click-counter-client.war deploy/
ln -sf ../../src/backend/click-counter-hz-server/target/click-counter-hz-server.jar deploy/

ln -sf ../../deploy/click-counter-client.war jetty/webapps/


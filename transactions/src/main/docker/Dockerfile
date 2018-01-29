FROM jboss/wildfly:8.2.1.Final

RUN /opt/jboss/wildfly/bin/add-user.sh admin passw0rd --silent

CMD /opt/jboss/wildfly/bin/standalone.sh -b=0.0.0.0 -bmanagement=0.0.0.0 --debug
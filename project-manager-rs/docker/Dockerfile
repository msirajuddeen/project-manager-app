FROM openjdk:8
ADD /project-manager-rs-0.0.1-SNAPSHOT.jar project-manager-rs-0.0.1-SNAPSHOT.jar
EXPOSE 8090
RUN bash -c 'touch /project-manager-rs-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","project-manager-rs-0.0.1-SNAPSHOT.jar"]

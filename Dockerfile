FROM maven:3.5.3-jdk-8 as builder

COPY . /build
WORKDIR /build
RUN mvn package -Dmaven.test.skip=true

FROM jetty:9-jre8-alpine as runner
USER jetty:jetty
COPY --from=builder ./build/target/cqf-ruler.war /var/lib/jetty/webapps/cqf-ruler.war
EXPOSE 8080
ENV JAVA_OPTIONS="-Dfhir.baseurl.r4=http://localhost:8080/cqf-ruler -Dfhir.baseurl.dstu3=http://localhost:8080/cqf-ruler -Dfhir.baseurl.dstu2=http://localhost:8080/cqf-ruler"

# Assumes the existence of a stu3 directory
# TODO: runtime mounting. Need to use gosu or similar to handle permisions correctly.
FROM runner as test-data
RUN mkdir -p /var/lib/jetty/target
COPY --chown=jetty:jetty ./target/stu3  /var/lib/jetty/target/stu3
FROM ruudud/leiningen

ONBUILD ADD . /app
ONBUILD WORKDIR /app
ONBUILD RUN lein ring uberjar
ONBUILD RUN mv target/*-standalone.jar target/app-standalone.jar

ENTRYPOINT ["java", "-jar", "/app/target/app-standalone.jar"]

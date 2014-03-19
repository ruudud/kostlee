FROM hrafique/openjdk-quantal

RUN apt-get update
RUN apt-get install -y curl

RUN curl https://raw.github.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein
RUN chmod a+x /usr/local/bin/lein

ENV LEIN_ROOT 1
RUN lein -v

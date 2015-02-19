FROM yaronr/openjdk-7-jre

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN curl -L https://raw.github.com/technomancy/leiningen/stable/bin/lein > /usr/local/bin/lein
RUN chmod a+x /usr/local/bin/lein

ENV LEIN_ROOT 1
RUN lein -v

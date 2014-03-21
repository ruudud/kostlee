.PHONY: all install dependencies docker dockerdep aufs stack

all:
		# Install leiningen with `make install`.
		#
		# Type `sudo make docker` to install containers and dependencies.
		# Optional: `sudo usermod -aG docker <your_username>` to run docker commands without sudo.
		# Then you can run `docker run -d -e PORT=5000 -p 5000 ruudud/kostlee`

install: lein

lein:
	wget https://raw.github.com/technomancy/leiningen/stable/bin/lein
	chmod u+x lein
	./lein -v

docker: dependencies stack app

app:
	@docker build -t ruudud/kostlee .

stack: 
	@docker images | grep ruudud/leiningen || docker build -t ruudud/leiningen contrib/leiningen
	@docker images | grep ruudud/uberjar || docker build -t ruudud/uberjar contrib/uberjar

dependencies: dockerdep

dockerdep: aufs
	egrep -i "^docker" /etc/group || groupadd docker
	curl https://get.docker.io/gpg | apt-key add -
	echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list
	apt-get update
ifdef DOCKER_VERSION
	apt-get install -y lxc-docker-${DOCKER_VERSION}
else
	apt-get install -y lxc-docker
endif
	sleep 2 # give docker a moment i guess

aufs:
	lsmod | grep aufs || modprobe aufs || apt-get install -y linux-image-extra-`uname -r`


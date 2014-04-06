#!/bin/bash
set -eo pipefail
#set -x

port=3000
docker_opts="-v /srv:/srv:ro -p $port"

if [[ "$#" -ne 2 ]] || [[ "$1" =~ (^h(elp)?$) ]]; then
    echo "Usage: $0 <vhost> <container name>"
    exit 1
fi

vhost="$1"
name="$2"

# Assume $port is unique for this app :-)
set +eo pipefail
old_container_ids=$(docker ps | grep "$port/tcp" | cut -d" " -f 1)
set -eo pipefail

logger "$(date --iso-8601=seconds) - Starting deploy of $vhost..."
cd "$appdir"
cid=$(docker run -d $docker_opts "$name")
caddr=$(docker port $cid 3000 \
        | sed 's,0.0.0.0,http://localhost,')

# Wait for startup
sleep 15

# Add (and activate) new back-end
redis-cli rpush "frontend:$vhost" "$caddr"
# Ensure newly added back-end is first in list
redis-cli lset "frontend:$vhost" 1 "$caddr"
# Remove all but 1 back-end
redis-cli ltrim "frontend:$vhost" 0 1

logger "$(date --iso-8601=seconds) - New back-end activated."

if [[ ! -z "$old_container_ids" ]]; then
  logger "$(date --iso-8601=seconds) - Removing $old_container_ids..."
  echo $old_container_ids | xargs docker stop
  echo $old_container_ids | xargs docker rm
fi

logger "$(date --iso-8601=seconds) - Finished redeploying '$name' for $vhost."

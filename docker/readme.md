
# For dev/prod - start mongo and seed
docker-compose build
docker-compose up

# Create backup from running container
docker run --rm --volumes-from docker_mongodata_1 -v $(pwd)/backup:/backup mongo tar cvf /backup/backup.tar /data

# build IT image with backup

mkdir -p /backup/seed/data/db /backup/seed/data/configdb

docker run -d -p 27017:27017 -v /backup/0/data/db:/data/db -v 
/backup/0/data/configdb:/data/configdb db mongo




docker build -t mongo-base mongo-base
docker build -t mongo mongo

docker build -t mongo-data mongo-data

docker build -t mongo-seed mongo-seed

docker run -d -p 27017:27017 -h mongodb mongo
docker run --rm --volumes-from ada25b375b4f mongo-seed

#backup the /data folder
docker run --rm --volumes-from ada25b375b4f -v $(pwd):/backup mongo tar cvf /backup/backup.tar /data

# create fresh container with data volume
docker run -v /data --name dbstore2 mongo true
# untar backup in the container/data volume
docker run --rm --volumes-from dbstore2 -v $(pwd):/backup mongo bash -c "cd /data && tar xvf /backup/backup.tar --strip 1"



FROM mongo
MAINTAINER gregoire.cacheux@gmail.com

ADD mongo_data.tar.gz /data

COPY docker-entrypoint.sh /runAndImport.sh
ENTRYPOINT ["/runAndImport.sh"]
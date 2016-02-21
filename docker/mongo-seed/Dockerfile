FROM mongo
MAINTAINER gregoire.cacheux@gmail.com

RUN curl https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/dataset.json > dataset.json
RUN chmod +x /entrypoint.sh

CMD mongoimport --host mongodb --db test --collection restaurants --drop --file dataset.json
# jackson-mongo-codec
Provides a Mongo Java driver 3.0 Codec based on Jackson

## Import Mongo Example Dataset
From https://docs.mongodb.org/getting-started/shell/import-data/

    curl https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/dataset.json > primer-dataset.json

    mkdir -p ~/mongo/data
    mongod --dbpath ~/mongo/data

    mongoimport --db test --collection restaurants --drop --file primer-dataset.json

## Mongo Java driver 3.0
https://docs.mongodb.org/ecosystem/drivers/java/


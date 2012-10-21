elasticsearch-javaee
====================

## Getting Started
### ElasticSearch
```sh
cd ${elasticsearch.home}/bin
./elasticsearch
```
The default mapping for the ```supplier.name``` is not enough so we need to set the ```french``` analyzer.

If the index is already generated, just ```DELETE``` it :
```sh
curl -XDELETE 'http://localhost:9200/financial'
```

Then execute the command below : 
```sh
curl -XPUT 'http://localhost:9200/financial/invoice/_mapping?pretty' -d '{ 
  "properties" : { 
    "supplier.name" : { 
      "type" : "string", 
      "analyzer" : "french" 
    } 
  } 
}'
```

### Run with Arquillian



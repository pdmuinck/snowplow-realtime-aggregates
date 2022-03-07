# Real time aggregates platform

Real time raw data is nice, real time aggregate data is way more awesome.

Imagine you are a media company that generates 10k of web/app traffic events per second. You managed to get this data in your warehouse near real time. Congratulations! Downstream projects now consume your near real time data. Some of them want to report on specific dimensions of your data and need to write a query on your data warehouse to get the results. Afterwards they schedule the operation to be repeated every 30s. The downstream aggregation functions turn out to be compute intensive because they run in big chunks of data. If you would aggregate your real time data you will end up with lower compute time of your downstream processes, more efficient and performance reports, and less storage costs. Instead of having your 20 downstream processes to write and  schedule their aggregates in the data warehouse, you create the aggregates in the data warehouse. This ahhrehate platform does this for you.

## How this platform works?
This platform creates windowed streams on your source data.
Provide a source stream and destination sink, select your dimensions you want to aggregate on 
together with the aggregate functions and your time window.

## Example
You have an enriched snowplow kinesis stream on which you want to count the number if page views
per app ID every 10 seconds and you want to store it on Kafka.

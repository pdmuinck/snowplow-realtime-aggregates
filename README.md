# Snowplow real time aggregates

Real time raw data is nice, real time aggregate data is way more awesome.

Imagine you are a media company that generates 10k of web/app traffic events per second. 
You managed to get this data in your warehouse near real time. 
Congratulations! 
Now downstream procsses can now consume your near real time data. 
Some of them want to report on specific dimensions of your data.
They will often end up writing event time windowed queries on your data warehouse to get the data that feeds their report. 
The downstream aggregation functions turn out to be compute intensive because they run on big chunks of data. 
If you aggregate your real time data you will end up with a lower amount of storage, lower compute time for real time queries and more efficient and performant reports. 
Instead of having your 20 downstream processes to write and schedule their aggregates in the data warehouse, you create the aggregates in real time and store them in the data warehouse.

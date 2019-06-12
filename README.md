# TEAStream

## Introduction

For more information on this project, you can read this series of articles on the [Bleckwen's Medium tech blog](https://medium.com/bleckwen):

- [Twitter sentiment analysis in real time, part 1.html](https://medium.com/bleckwen/twitter-sentiment-analysis-in-real-time-part-1-be3fcc0bb4a2) (available)
- [Twitter sentiment analysis in real time, part 2.html]() (not currently available)
- [Twitter sentiment analysis in real time, part 3.html]() (not currently available)

Time to deploy the project!

## 0. Useful scripts

If you want to skip ahead the next parts, you can use the .sh files:

- `build.sh`: create the .jar files and build the docker images locally.
- `start.sh`: launch the app.
- `start-all.sh`: both build.sh and start.sh combined.

In this case, jump to part 5.

## 1. Create the .jar

You will need 2 .jar files created through sbt assembly:

```
sbt kafkatwitter/assembly
sbt flink/assembly
```

The files are located respectively in `deploy/producer-job` and `deploy/flink-jobs`.

## 2. Create the Docker images

Go to the `deploy` directory:

```
cd deploy/
```

There are 4 images to build in total:

- **Busybox**
```
cd busybox/
docker build . -t busybox
```
- **Web App**  
```
cd web-app/
docker build . -t flask
```
- **Flink Job** 
```
cd flink-jobs/
docker build . -t flink-job
```
- **Producer Job**  
```
cd producer-job/
docker build . -t producer-anon-job
```

## 3. Add Twitter credentials

Open the `.env` file with your favorite text editor and write down your credentials: `CONSUMER_KEY`, `CONSUMER_SECRET`, `ACCESS_TOKEN`, `ACCESS_TOKEN_SECRET`.

## 4. Launch the project

```
docker-compose up
```
If you want to run the process in the background, add the `-d` option.

## 5. Use the App

Open [localhost:5000](https://localhost:5000) in your web browser. Type the word(s) you want to track and the app will do the rest.  
You can look at the results on Kibana at [localhost:5601](https://localhost:5601).
> run `sudo sysctl -w vm.max_map_counts=262144` if elasticsearch returns `max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]`.

For index pattern, you can use **flink-index**. The dashboard presented in the articles is available at `deploy/export.json` and can be imported.

## 6. Other tools

You can check the flink job with the flink dashboard: [localhost:8081](http://localhost:8081).  
You can check available topics with the kafka-ui: [localhost:8000](http://localhost:8000).  
You can check available connectors with the connect-ui: [localhost:8003](http://localhost:8003).  

## 7. Stop the app

```
docker-compose down
```
The services will be shut down and the containers removed.  

Alternatively, you can run `stop.sh` if you used one of the previous scripts.

## License

License: CC BY-NC-SA 4.0

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.

All notebooks are licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International Public License.

A summary of this license is as follows.

You are free to:

    Share — copy and redistribute the material in any medium or format
    Adapt — remix, transform, and build upon the material

    The licensor cannot revoke these freedoms as long as you follow the license terms.

Under the following terms:

    Attribution — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.

    NonCommercial — You may not use the material for commercial purposes.

    ShareAlike — If you remix, transform, or build upon the material, you must distribute your contributions under the same license as the original.

    No additional restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.

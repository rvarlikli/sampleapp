# README #

##### Requires:
* Gradle 5.x
* Cassandra DB 3.+
* Java 1.8+
* Docker
* Kubernetes Cluster 1.15+
* Helm 2.14

# Database configuration
    - Install Cassandra
    - Configure cassandra to run on the wished port or ports provided in application.yml files.
        - customizing cassandra address or/and port will require updating application.[env].yum files for cassandra settings.
    - Run schema.cql script before building or starting the project, you can execute the script via preferred client or terminal to initialise database schema.

# Running instructions on local

    - Run the project with spring boot 
    ```sh
        $ gradle bootRun -Dspring.config.location=classpath:application-local.yml
    ```

##### Create new version of docker image
Create tag for a branch or release
```sh
git tag v0.1
```

Create an image
```sh
gradle clean jibDockerBuild
```
Running docker container on local
```sh
IMAGE_VERSION=$(git describe --tag --long)
docker run -d --name sampleapp -e "SPRING_PROFILES_ACTIVE=${environment}" -p 8081:8080 cloudnesil/sampleapp:${IMAGE_VERSION}
```
Show Docker logs
```sh
docker logs -f sampleapp
```

### Deploy to Kubernetes with Helm
Note: You must package and push to helm repo first from helm-chart repo. Check README file for steps.
Tested on Kubernetes v1.15 with Helm 2.14+

#### with ingress integration
```sh
helm repo add mychartmuseum chartmuseum.example.com

helm upgrade --install sample-app --namespace default cloudnesil/sampleapp --version 0.6.0 -f kubernetes/dev/values.yml
```

#### with nodeport

```sh

helm upgrade --install sample-app --namespace default cloudnesil/sampleapp --version 0.6.0 -f kubernetes/dev-nodeport/values.yml

### To get NodePort
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services sample-app-sampleapp)

```


#### to set specific docker image version

```sh

helm upgrade --install sample-app --namespace default cloudnesil/sampleapp --version 0.6.0 --set image.tag=${IMAGE_VERSION} -f kubernetes/dev/values.yml
```








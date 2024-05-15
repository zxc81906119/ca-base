# 乾淨架構專案模板

未來 CUB 使用的專案基本模板

## Image 位置

於 docker hub 中的儲存庫 :

```bash
open https://hub.docker.com/repository/docker/minggod/clean-base/
```

## 環境變數

| No | 變數                                      | 說明                         | 預設值                                                                              |
|----|-----------------------------------------|----------------------------|----------------------------------------------------------------------------------|
| 1  | spring_datasource_username              | DB 使用者                     | sa                                                                               |
| 2  | spring_datasource_password              | DB 密碼                      | p@ssw0rd                                                                         |
| 3  | spring_datasource_url                   | DB 連線位置                    | jdbc:h2:mem:test                                                                 |
| 4  | spring_datasource_driver_class_name     | DB 驅動                      | org.h2.Driver                                                                    |
| 5  | spring_jpa_database                     | DB 類型                      | h2                                                                               |
| 6  | management_otlp_tracing_endpoint        | openTelemetry collector 位置 | http://otel-http-cub-dev.apps.cluster-nwlbs.dynamic.redhatworkshops.io/v1/traces |
| 7  | management_tracing_sampling_probability | tracing 採樣率                | 1.0                                                                              |

## 應用基本資訊

* Health Check Endpoint
    * overall
      ```bash
      curl http://localhost:8080/actuator/health
      ```
    * liveness
      ```bash
      curl http://localhost:8080/actuator/health/liveness    
      ```
    * readiness
      ```bash
      curl http://localhost:8080/actuator/health/readiness
      ```

* Prometheus Exporter

```bash
curl http://localhost:8080/actuator/prometheus
```

* Swagger UI

```bash
open http://localhost:8080/swagger-ui.html
```

## 應用建置

1. 程式碼品質掃描

```bash
export PROJECT_KEY=""
export PROJECT_NAME=""
export SONAR_URL="http://localhost:9000"
export SONAR_TOKEN=""
mvn clean verify sonar:sonar -Dsonar.projectKey="${PROJECT_KEY}" -Dsonar.projectName="${PROJECT_NAME}" -Dsonar.host.url="${SONAR_URL}" -Dsonar.token="${SONAR_TOKEN}"

```

2. 建置 Java Artifact

```bash
mvn clean package || exit 1
ls target
```

3. 檢查測試覆蓋率

```bash
mvn clean verify

```

3. 啟動應用

```bash
export JAR_NAME="$(mvn -Dexpression=project.build.finalName -DforceStdout -q help:evaluate)"
if [ -z "${JAR_NAME}" ]; then
  echo 'Jar Name Not Found'
  exit 1
fi
java -jar "target/${JAR_NAME}.jar"

```

## 容器化

1. 建置容器並推送儲存庫

```bash
export USERNAME="zxc81906119@gmail.com"
export PASSWORD=""
export BASE_IMAGE="registry.access.redhat.com/ubi8/openjdk-17"
export BASE_IMAGE_TAG="1.19-1"
export IMAGE_REPO_HOST_PORT="docker.io"
export IMAGE_REPO_ORGANIZATION="minggod"
export IMAGE_REPO="${IMAGE_REPO_HOST_PORT}/${IMAGE_REPO_ORGANIZATION}/clean-base"
export GIT_HASH="$(git rev-parse --short=14 HEAD)"
if [ -z "${GIT_HASH}" ]; then
  echo 'Git Hash Not Found'
  exit 1
fi
podman logout "${IMAGE_REPO_HOST_PORT}"
podman login -u "${USERNAME}" -p "${PASSWORD}" --tls-verify=false ${IMAGE_REPO_HOST_PORT} || exit 1
podman pull "${IMAGE_REPO}:${GIT_HASH}"
if [ "$?" -ne 0 ]; then
  podman pull "${BASE_IMAGE}:${BASE_IMAGE_TAG}" || exit 1
  podman build -f container/file/Containerfile --build-arg BASE_IMAGE_TAG="${BASE_IMAGE_TAG}" -t "${IMAGE_REPO}:${GIT_HASH}" . || exit 1
  podman push "${IMAGE_REPO}:${GIT_HASH}" || exit 1
fi

podman tag "${IMAGE_REPO}:${GIT_HASH}" "${IMAGE_REPO}:latest" || exit 1
podman push "${IMAGE_REPO}:latest" || exit 1
```

2. 啟動容器

```bash
export IMAGE_REPO_HOST_PORT="docker.io"
export IMAGE_REPO_ORGANIZATION="minggod"
export IMAGE_REPO="${IMAGE_REPO_HOST_PORT}/${IMAGE_REPO_ORGANIZATION}/clean-base"
export HOST_PORT="8080"
podman run -p "${HOST_PORT}:8080" "${IMAGE_REPO}:latest"

```

## 佈署於 OpenShift

```bash
export CLUSTER_URL=""
export NAMESPACE=""
export USERNAME=""
export PASSWORD=""
export APP_NAME="clean-base"
# 登入叢集
oc logout
oc login --insecure-skip-tls-verify -u "${USERNAME}" -p "${PASSWORD}" "${CLUSTER_URL}" || exit 1
# Apply YAML
oc apply -n "${NAMESPACE}" -f container/k8s/ || exit 1
oc rollout restart "deployment/${APP_NAME}-deployment" -n "${NAMESPACE}" || exit 1
oc logout

```
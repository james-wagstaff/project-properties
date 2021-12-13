package com.identifix.projectproperties.kubernetes.impl.runners;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunner {

    @SneakyThrows
    public Future<?> runCommand(String command, Consumer<String> consumer) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("cmd.exe", "/c", command);
        InputStream process = builder.start().getInputStream();
        //InputStream process = getDummyNamespaces();
        StreamGobbler streamGobbler =
            new StreamGobbler(process, consumer);
        return Executors.newSingleThreadExecutor().submit(streamGobbler);
    }

    private InputStream getDummyIngressData() {
        String data = """
            Name:             toyota-publish-support-service
            Namespace:        smr-dev-identifix-cms
            Address:          10.132.67.11,10.132.67.12,10.132.67.13,10.132.67.14,10.132.67.15,10.132.67.30,10.132.67.32,10.132.67.33,10.132.67.34,10.132.67.35
            Default backend:  default-http-backend:80 (<error: endpoints "default-http-backend" not found>)
            Rules:
              Host                                                           Path  Backends
              ----                                                           ----  --------
              toyota-publish-support-service.tkg-smr-dev.usdc01.solera.farm \s
                                                                             /   toyota-publish-support-service:8080 (100.96.254.77:8080,100.96.95.165:8080)
            Annotations:                                                     nginx.ingress.kubernetes.io/rewrite-target: /
            Events:                                                          <none>
                        
            """;
        return new ByteArrayInputStream(data.getBytes());
    }

    private InputStream getDummyServiceData() {
        String csv = """
                NAME                                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)    AGE
                aces-to-oem-service                  ClusterIP   100.97.60.20     <none>        8080/TCP   56d
                chrysler-data-drop-page-service      ClusterIP   100.97.27.149    <none>        8080/TCP   45d
                chrysler-data-drop-publish-service   ClusterIP   100.97.105.193   <none>        8080/TCP   42d
                chrysler-publish-support-service     ClusterIP   100.97.249.214   <none>        8080/TCP   45d
                cms-content-availability-service     ClusterIP   100.97.131.88    <none>        8080/TCP   34d
                cms-service-s3                       ClusterIP   100.97.119.93    <none>        8080/TCP   47d
                content-conversion-service           ClusterIP   100.97.88.114    <none>        8080/TCP   42d
                content-labeling-service             ClusterIP   100.97.41.192    <none>        8080/TCP   45d
                crawler-dashboard-service            ClusterIP   100.97.58.129    <none>        8080/TCP   45d
                crawler-error-service                ClusterIP   100.97.134.159   <none>        8080/TCP   47d
                crawler-metalink-service             ClusterIP   100.97.162.56    <none>        8080/TCP   83d
                crawler-page-service                 ClusterIP   100.97.80.205    <none>        8080/TCP   42d
                crawler-service                      ClusterIP   100.97.48.126    <none>        8080/TCP   39d
                crawler-type-service                 ClusterIP   100.97.11.115    <none>        8080/TCP   47d
                credential-service                   ClusterIP   100.97.253.107   <none>        8080/TCP   47d
                data-drop-extraction-service         ClusterIP   100.97.163.88    <none>        8080/TCP   47d
                data-drop-fetch-scheduler            ClusterIP   100.97.140.207   <none>        8080/TCP   46d
                data-drop-fetch-service              ClusterIP   100.97.246.93    <none>        8080/TCP   47d
                emmett                               ClusterIP   100.97.158.201   <none>        80/TCP     46d
                emmett-service                       ClusterIP   100.97.115.95    <none>        8080/TCP   47d
                fett                                 ClusterIP   100.97.179.123   <none>        8080/TCP   25d
                fett-service                         ClusterIP   100.97.81.56     <none>        8080/TCP   40d
                ford-crawler-fetch-service           ClusterIP   100.97.37.151    <none>        8080/TCP   83d
                kraken-service                       ClusterIP   100.97.9.241     <none>        8080/TCP   40d
                kraken-vehicle-support-service       ClusterIP   100.97.228.50    <none>        8080/TCP   34d
                nuxeo                                ClusterIP   100.97.107.58    <none>        8080/TCP   41d
                oem-persistence                      ClusterIP   100.97.218.7     <none>        8080/TCP   40d
                oem-vehicle-descriptor-service       ClusterIP   100.97.172.36    <none>        8080/TCP   48d
                oemtools                             ClusterIP   100.97.64.29     <none>        80/TCP     18d
                publish-service                      ClusterIP   100.97.173.210   <none>        8080/TCP   43d
                publish-support-service              ClusterIP   100.97.149.132   <none>        8080/TCP   43d
                text-extractor-service               ClusterIP   100.97.151.218   <none>        8080/TCP   14d
                toyota-data-drop-page-service        ClusterIP   100.97.148.102   <none>        8080/TCP   46d
                toyota-data-drop-publish-service     ClusterIP   100.97.253.113   <none>        8080/TCP   45d
                toyota-publish-support-service       ClusterIP   100.97.2.82      <none>        8080/TCP   46d
            """;
        return new ByteArrayInputStream(csv.getBytes());
    }

    private InputStream getDummyServiceInfo() {
        String data = """
            Name:                   toyota-publish-support-service
            Namespace:              smr-dev-identifix-cms
            CreationTimestamp:      Thu, 14 Oct 2021 16:07:08 -0500
            Labels:                 solera-env=dev
                                    solera-ingress=enable
                                    solera-org=identifix-cms
                                    solera-vertical=smr
            Annotations:            deployment.kubernetes.io/revision: 2
            Selector:               service=toyota-publish-support-service
            Replicas:               2 desired | 2 updated | 2 total | 2 available | 0 unavailable
            StrategyType:           RollingUpdate
            MinReadySeconds:        0
            RollingUpdateStrategy:  25% max unavailable, 1 max surge
            Pod Template:
              Labels:           network/default=true
                                service=toyota-publish-support-service
              Service Account:  default
              Containers:
               toyota-publish-support-service:
                Image:      feeds.axadmin.net/docker/rc/toyota-publish-support-service:latest
                Port:       8080/TCP
                Host Port:  0/TCP
                Limits:
                  ephemeral-storage:  500M
                  memory:             3G
                Liveness:             http-get http://:8080/actuator/health delay=60s timeout=100s period=60s #success=1 #failure=10
                Environment:
                  CRAWLER-TYPE_NUMBER-OF-REQUESTS-BETWEEN-DATE-CHECKS:    64
                  CRAWLER-TYPE_TIME-BETWEEN-TYPE-REFRESH-IN-MS:           3600000
                  CRAWLER_AUDI-DATA-DROP-EXTRACTION-EXCHANGE-NAME:        audi-data-drop-extraction-exchange
                  CRAWLER_AUDI-DATA-DROP-PAGE-EXCHANGE-NAME:              audi-data-drop-page-exchange
                  CRAWLER_AUDI-DATA-DROP-PUBLISH-EXCHANGE-NAME:           audi-data-drop-publish-exchange
                  CRAWLER_BACK-OFF-INITIAL-INTERVAL:                      1000
                  CRAWLER_BACK-OFF-MAX-INTERVAL:                          10000
                  CRAWLER_BACK-OFF-MULTIPLIER:                            2
                  CRAWLER_CHRYSLER-DATA-DROP-EXTRACTION-EXCHANGE-NAME:    chrysler-data-drop-extraction-exchange
                  CRAWLER_CHRYSLER-DATA-DROP-PAGE-EXCHANGE-NAME:          chrysler-data-drop-page-exchange
                  CRAWLER_CHRYSLER-DATA-DROP-PUBLISH-EXCHANGE-NAME:       chrysler-data-drop-publish-exchange
                  CRAWLER_DEAD-LETTER-EXCHANGE-NAME:                      dead-letter-office
                  CRAWLER_ERROR-EXCHANGE-NAME:                            error-exchange
                  CRAWLER_FAILURE-EXCHANGE-NAME:                          failure-exchange
                  CRAWLER_FORD-FETCH-EXCHANGE-NAME:                       ford-fetch-exchange
                  CRAWLER_MERCEDES-FETCH-EXCHANGE-NAME:                   mercedes-fetch-exchange
                  CRAWLER_MERCEDES-PAGE-EXCHANGE-NAME:                    mercedes-publish-exchange
                  CRAWLER_MERCEDES-PUBLISH-EXCHANGE-NAME:                 mercedes-publish-exchange
                  CRAWLER_MERCEDES-WIS-FETCH-EXCHANGE-NAME:               mercedes-wis-fetch-exchange
                  CRAWLER_META-LINK-EXCHANGE-NAME:                        metaLink-exchange
                  CRAWLER_PAGE-EXCHANGE-NAME:                             crawler-page-exchange
                  CRAWLER_PUBLISH-EXCHANGE-NAME:                          publish-exchange
                  CRAWLER_TOYOTA-DATA-DROP-EXTRACTION-EXCHANGE-NAME:      toyota-data-drop-extraction-exchange
                  CRAWLER_TOYOTA-DATA-DROP-PAGE-EXCHANGE-NAME:            toyota-data-drop-page-exchange
                  CRAWLER_TOYOTA-DATA-DROP-PUBLISH-EXCHANGE-NAME:         toyota-data-drop-publish-exchange
                  CRAWLER_VOLKSWAGEN-DATA-DROP-EXTRACTION-EXCHANGE-NAME:  volkswagen-data-drop-extraction-exchange\s
                  CRAWLER_VOLKSWAGEN-DATA-DROP-PAGE-EXCHANGE-NAME:        volkswagen-data-drop-page-exchange
                  CRAWLER_VOLKSWAGEN-DATA-DROP-PUBLISH-EXCHANGE-NAME:     volkswagen-data-drop-publish-exchange
                  GOOGLE_VISION_KEY:                                      AIzaSyBGQDFsFPe_DS0qfluq-fmLdq1ZsTC7IE4
                  GOOGLE_VISION_URL:                                      https://vision.googleapis.com/v1/images:annotate?key=
                  S3_ACCESS_KEY:                                          identifixcontent-dev-admin
                  S3_ENDPOINT:                                            s3-us.solera.farm
                  S3_SECRET_KEY:                                          aBG+HxCNDNnWM2g9Q3eYkkHbNJF4TlHy0RVPoMdA
                  VAULT_TOKEN_RENEW_RATE:                                 9000000
                  VAULT_TOKEN_TTL:                                        604800000
                  CRAWLER-TYPE_SERVER-URI:                                https://crawler-type-service.tkg-smr-dev.usdc01.solera.farm
                  CREDENTIAL-CLIENT_SERVER-URI:                           https://credential-service.tkg-smr-dev.usdc01.solera.farm
                  SPRING_PROFILES_ACTIVE:                                 dev
                  SPRING_RABBITMQ_HOST:                                   dev-us01-nrmq-vip.solera.farm
                  SPRING_RABBITMQ_PASSWORD:                               zlm666wFdgW3vGrV
                  SPRING_RABBITMQ_PORT:                                   5672
                  SPRING_RABBITMQ_SSL_ALGORITHM:                          TLSv1.2
                  SPRING_RABBITMQ_SSL_ENABLED:                            false
                  SPRING_RABBITMQ_USERNAME:                               smr-dev-identifix-cms
                  SPRING_RABBITMQ_VIRTUAL-HOST:                           smr-dev-identifix-cms
                  CONFIGSCOPE:                                            Development environment properties for toyota-publish-support-service
                  FILE-STORE_TIME-STAMP-FORMAT:                           dd_MMM_yyyy-HH_mm_ss_S
                  FILE-STORE_TOYOTA-BUCKET-NAME:                          toyota-crawler-bucket
                  FILE-STORE_TOYOTA-ZIP-PATH:                             dev/fetchedZip/
                  SPRING_DATA_NEO4J_PASSWORD:                             obeche_wagtail_dacron_kahlua
                  SPRING_DATA_NEO4J_URI:                                  bolt://oemut08.mpifix.com:7688
                  SPRING_DATA_NEO4J_USERNAME:                             neo4j
                  SPRING_PROFILES:                                        dev
                Mounts:                                                   <none>
              Volumes:                                                    <none>
            Conditions:
              Type           Status  Reason
              ----           ------  ------
              Progressing    True    NewReplicaSetAvailable
              Available      True    MinimumReplicasAvailable
            OldReplicaSets:  <none>
            NewReplicaSet:   toyota-publish-support-service-54988f584c (2/2 replicas created)
            Events:          <none>
            """;
        return new ByteArrayInputStream(data.getBytes());
    }

    private InputStream getDummyNamespaces() {
        String data = """
            NAME                         STATUS   AGE
            default                      Active   100d
            fluent-bit                   Active   26d
            imagepullsecret-patcher      Active   99d
            ingress-nginx                Active   99d
            kube-node-lease              Active   100d
            kube-public                  Active   100d
            kube-system                  Active   100d
            pinniped-concierge           Active   100d
            pinniped-supervisor          Active   100d
            smr-dev-dst                  Active   63d
            smr-dev-dst-acdelco          Active   63d
            smr-dev-dst-fmp              Active   63d
            smr-dev-hollander            Active   93d
            smr-dev-iatn2                Active   85d
            smr-dev-identifix-cms        Active   98d
            smr-dev-identifix-dh         Active   79d
            smr-solera-apu-development   Active   90d
            tanzu-observability-saas     Active   64d
            tkg-system                   Active   100d
            tkg-system-public            Active   100d
            vault-secrets-webhook        Active   65d
            velero                       Active   12d
            vmware-system-tmc            Active   100d
            weave                        Active   58d
            """;
        return new ByteArrayInputStream(data.getBytes());
    }
}

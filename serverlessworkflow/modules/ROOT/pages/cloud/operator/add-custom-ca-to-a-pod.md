# Adding a custom CA Certificate to a Container Running Java

If you're working with containers running Java applications and need to add a CA (Certificate Authority) certificate for secure communication, you can follow these steps. This guide assumes you are familiar with containers and have basic knowledge of working with YAML files.

Table Of Contents:
1. [Problem Space](#problem-space)
2. [Step 1: Obtain the CA Certificate](#step-1-obtain-the-ca-certificate)
3. [Step 2: Prepare a trust store in an init-container](#step-2-prepare-a-trust-store-in-an-init-container)
4. [Step 3: Configure Java to load the new keystore](#step-3-configure-Java-to-load-the-new-keystore)
5. [Full working example](#full-working-example)
6. [Serverless Workflow Example](#serverless-workflow-example)

## Problem Space 

If you have a containerized Java application that connects to an SSL endpoint with a certificate signed by an internal authority (like SSL terminated routes on a cluster) you need to make sure Java can read the CA Authority certificate and verify it. Java unfortunately doesn't load certificates directly, but rather store them in a [keystore][keystore].

The default trust store under `$JAVA_HOME/lib/security/cacerts` contains only CA's which are shipped with the Java distribution and there's the `keytool` tool that knows how manipulate those key stores.
The containerized application may not know the CA certificate in build time, and so we need to add it to the trust-store in deployment. To automate that we can a combination of an init-container and a shared directory to pass the mutated trust store to the container before it runs. Let's run this step by step:

## Step 1: Obtain the CA Certificate

Before proceeding, ensure you have the CA certificate file (in PEM format) that you want to add to the Java container. If you don't have it, you may need to obtain it from your system administrator or certificate provider.

For the purpose of this guide we would take the k8s cluster root CA that is automatically deployed into every container under `/var/run/secrets/kubernetes.io/serviceaccount/ca.crt`

## Step 2: Prepare a trust store in an init-container

Add or amend this volumes and init-container snippet to your pod spec or podTemplate in a deployment:

```yaml
spec:
  volumes:
    - name: new-cacerts
      emptyDir: {}
  initContainers:
    - name:  add-kube-root-ca-to-cacerts
      image: registry.access.redhat.com/ubi9/openjdk-21
      volumeMounts:
        - mountPath: /opt/new-cacerts
          name: new-cacerts 
      command:
        - /bin/bash
        - -c
        - |
          cp $JAVA_HOME/lib/security/cacerts /opt/new-cacerts/
          chmod +w /opt/new-cacerts/cacerts
          keytool -importcert -no-prompt -keystore /opt/new-cacerts/cacerts -storepass changeit -file /var/run/secrets/kubernetes.io/serviceaccount/ca.crt 
```

The default keystore under $JAVA_HOME is part of the container image and is not mutable. We have to create the mutated copy to a shared volume, hence the 'new-cacerts' one.

## Step 3: Configure Java to load the new keystore

Here we would just mount the new, modified cacerts into the default location where the JVM looks at.
The example main uses the standard http client so alternative we could mount the cacerts to a different location and
configure the Java runtime to load the new keystore with a system property `-Djavax.net.ssl.trustStore`.
Note that libraries like resteasy don't respect that flag and may need to programmatically set the trust store location.

```yaml
 containers:
   - command:
     - /bin/bash
     - -c
     - |
       curl -L https://gist.githubusercontent.com/rgolangh/b949d8617709d10ba6c690863e52f259/raw/bdea4d757a05b75935bbb57f3f05635f13927b34/Main.java -o curl.java
       java curl.java https://kubernetes
     image: registry.access.redhat.com/ubi9/openjdk-21
     imagePullPolicy: Always
     name: openjdk-21
     volumeMounts:
       - mountPath: /lib/jvm/java-21/lib/security
         name: new-cacerts
         readOnly: true
       - mountPath: /var/run/secrets/kubernetes.io/serviceaccount
         name: kube-api-access-5npmd
         readOnly: true
```

Notice the volume mount of the previously mutate keystore.


## Full working example

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: root-ca-to-cacerts
spec:
  initContainers:
    - name:  add-kube-root-ca-to-cacerts
      image: registry.access.redhat.com/ubi9/openjdk-21
      volumeMounts:
        - mountPath: /opt/new-cacerts
          name: new-cacerts 
      command:
        - /bin/bash
        - -c
        - |
          cp $JAVA_HOME/lib/security/cacerts /opt/new-cacerts/
          chmod +w /opt/new-cacerts/cacerts
          keytool -importcert -no-prompt -keystore /opt/new-cacerts/cacerts -storepass changeit -file /var/run/secrets/kubernetes.io/serviceaccount/ca.crt 
  containers:
    - command:
      - /bin/bash
      - -c
      - |
        curl -L https://gist.githubusercontent.com/rgolangh/b949d8617709d10ba6c690863e52f259/raw/bdea4d757a05b75935bbb57f3f05635f13927b34/Main.java -o curl.java
        java curl.java https://kubernetes
      image: registry.access.redhat.com/ubi9/openjdk-21
      imagePullPolicy: Always
      name: openjdk-21
      volumeMounts:
      - mountPath: /lib/jvm/java-21/lib/security/
        name: new-cacerts
        readOnly: true
      - mountPath: /var/run/secrets/kubernetes.io/serviceaccount
        name: kube-api-access-5npmd
        readOnly: true
  volumes:
  - name: new-cacerts
    emptyDir: {}
  - name: kube-api-access-5npmd
    projected:
      sources:
      - serviceAccountToken:
          path: token
      - configMap:
          items:
          - key: ca.crt
            path: ca.crt
          name: kube-root-ca.crt
```

## Serverless Workflow Example

Similar to a deployment spec a serverless workflow has a spec.podTemplate , with minor differences, but the change is almost identical.
In this case we are mounting some ingress ca bundle because we want our workflow to reach the `.apps.{custer-name}.{cluster-domain}` SSL endpoint.
Here is the relevant spec section of a workflow with the changes:

```yaml
#...
spec:
  flow:
  # ...
  podTemplate:
    container:
      volumeMounts:
      - mountPath: /lib/jvm/java-17/lib/security/
        name: new-cacerts
    initContainers:
    - command:
      - /bin/bash
      - -c
      - |
        cp $JAVA_HOME/lib/security/cacerts /opt/new-cacerts/
        chmod +w /opt/new-cacerts/cacerts
        keytool -importcert -no-prompt -keystore /opt/new-cacerts/cacerts -storepass changeit -file /opt/ingress-ca/ca-bundle.crt
      image: registry.access.redhat.com/ubi9/openjdk-21
      name: add-kube-root-ca-to-cacerts
      volumeMounts:
      - mountPath: /opt/new-cacerts
        name: new-cacerts
      - mountPath: /opt/ingress-ca
        name: ingress-ca
    volumes:
    - emptyDir: {}
      name: new-cacerts
    - configMap:
        name: default-ingress-cert
      name: ingress-ca
    - name: kube-api-access-5npmd
      projected:
        sources:
        - serviceAccountToken:
            path: token
        - configMap:
            items:
            - key: ca.crt
              path: ca.crt
            name: kube-root-ca.crt
```




[keystore]: https://docs.oracle.com/en/java/javase/21/docs/specs/man/keytool.html
[pods-with-cert-initiation]: https://gist.githubusercontent.com/rgolangh/90fa261c3a6a12bc1dbe89fa3ad4842b/raw/4875aeb353d47b471c453452e4862a1509161c88/pods-with-cert-init.yaml

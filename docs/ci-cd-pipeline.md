# CI/CD Pipeline Documentation

## Overview

The Jenkins pipeline automates the complete software delivery process from code commit to live deployment.

```
git push → GitHub → Webhook → Jenkins → Maven → WAR → Tomcat → ALB → Live
```

---

## Pipeline Stages

### Stage 1: Checkout
- Jenkins pulls the latest code from the `main` branch on GitHub
- Triggered automatically via GitHub webhook on every `git push`
- Logs the branch name, commit hash, and build number

### Stage 2: Build & Test
- Runs `mvn clean test package`
- Compiles all Java source files across controller, dao, service, model, filter, util packages
- Executes JUnit tests
- Packages the application as a WAR file (`ecommerce-app.war` ~14MB)
- Publishes JUnit test results to Jenkins

### Stage 3: Archive Artifact
- Saves the WAR file as a Jenkins build artifact
- Fingerprints the artifact for traceability
- WAR file available for download from Jenkins UI

### Stage 4: Deploy to AWS
- Uses `curl` to upload WAR to Tomcat Manager API
- Authenticates with stored Jenkins credentials (`tomcat-aws-credentials`)
- Uses `update=true` flag to hot-redeploy without server restart
- Tomcat undeploys old version and deploys new one automatically

### Stage 5: Smoke Test
- Waits 15 seconds for Tomcat to finish deployment
- Sends HTTP GET request to the app via ALB DNS
- Checks for HTTP 200 response
- Fails the build if app is not responding

---

## Environment Variables

| Variable | Description |
|---|---|
| `APP_NAME` | Application name (`ecommerce-app`) |
| `WAR_FILE` | Path to built WAR file |
| `TOMCAT_AWS_URL` | Tomcat Manager API URL |
| `TOMCAT_AWS_CRED` | Jenkins credential ID for Tomcat |
| `DEPLOY_PATH` | Context path (`/ecommerce-app`) |
| `ALB_DNS` | ALB DNS for smoke test |

---

## Jenkins Credentials

| ID | Type | Purpose |
|---|---|---|
| `tomcat-aws-credentials` | Username/Password | Tomcat Manager API auth |

---

## Webhook Configuration

- **Trigger:** Every push to `main` branch
- **Payload URL:** `http://<jenkins-ip>:8080/github-webhook/`
- **Events:** Push only

---

## Build Time

| Stage | Approx Time |
|---|---|
| Checkout | ~1s |
| Build & Test | ~30s |
| Archive | ~1s |
| Deploy | ~10s |
| Smoke Test | ~20s |
| **Total** | **~1 min** |

---

## Rollback

To rollback to a previous build:
1. Jenkins → `ecommerce-pipeline` → Build History
2. Click the previous successful build
3. Click the WAR artifact to download
4. Manually deploy via Tomcat Manager UI:
   `http://<tomcat-ip>:8080/manager`

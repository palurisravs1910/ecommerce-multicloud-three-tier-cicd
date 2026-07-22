// ============================================================
// Jenkinsfile - E-Commerce CI/CD Pipeline
// Multi-Cloud: AWS (App Tier) + Azure (Backup/DR)
// Stack: GitHub → Jenkins → Maven → WAR → Tomcat
// ============================================================

pipeline {

    agent any

    // ---- Tool Declarations ----
    tools {
        maven 'Maven-3.9'    // Must match Jenkins Global Tool Configuration name
        jdk   'JDK-11'       // Must match Jenkins Global Tool Configuration name
    }

    // ---- Pipeline-wide Environment Variables ----
    environment {
        APP_NAME        = 'ecommerce-app'
        WAR_FILE        = 'target/ecommerce-app.war'

        // Tomcat on AWS (App Tier)
        TOMCAT_AWS_URL  = 'http://16.112.58.237:8080/manager/text'
        TOMCAT_AWS_CRED = 'tomcat-aws-credentials'     // Jenkins credential ID

        // Tomcat on Azure (Failover / DR)
        TOMCAT_AZ_URL   = 'http://your-azure-vm-ip:8080/manager/text'
        TOMCAT_AZ_CRED  = 'tomcat-azure-credentials'   // Jenkins credential ID

        DEPLOY_PATH     = '/ecommerce-app'
        NOTIFY_EMAIL    = 'devops@ecommerce.com'
    }

    stages {

        // ---- Stage 1: Checkout ----
        stage('Checkout') {
            steps {
                echo '==> Checking out source code from GitHub...'
                checkout scm
                echo "==> Branch: ${env.GIT_BRANCH}"
                echo "==> Commit: ${env.GIT_COMMIT}"
            }
        }

        // ---- Stage 2: Build & Test ----
        stage('Build & Test') {
            steps {
                echo '==> Running Maven clean, test, package...'
                sh 'mvn clean test package -DskipTests=false'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        // ---- Stage 3: Code Quality (SonarQube - optional) ----
        stage('Code Quality') {
            when {
                branch 'main'
            }
            steps {
                echo '==> Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=ecommerce-app'
                }
            }
        }

        // ---- Stage 4: Archive WAR ----
        stage('Archive Artifact') {
            steps {
                echo '==> Archiving WAR artifact...'
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
                echo "==> WAR archived: ${WAR_FILE}"
            }
        }

        // ---- Stage 5: Deploy to AWS Tomcat ----
        stage('Deploy to AWS') {
            when {
                anyOf {
                    branch 'main'
                    branch 'release/*'
                }
            }
            steps {
                echo '==> Deploying to AWS Tomcat (App Tier)...'
                withCredentials([usernamePassword(
                    credentialsId: "${TOMCAT_AWS_CRED}",
                    usernameVariable: 'TOMCAT_USER',
                    passwordVariable: 'TOMCAT_PASS'
                )]) {
                    sh """
                        curl -v --fail \
                          --upload-file ${WAR_FILE} \
                          "${TOMCAT_AWS_URL}/deploy?path=${DEPLOY_PATH}&update=true" \
                          --user "\${TOMCAT_USER}:\${TOMCAT_PASS}"
                    """
                }
                echo '==> AWS deployment complete.'
            }
        }

        // ---- Stage 6: Deploy to Azure (Failover) ----
        stage('Deploy to Azure') {
            when {
                branch 'main'
            }
            steps {
                echo '==> Deploying to Azure Tomcat (DR/Failover)...'
                withCredentials([usernamePassword(
                    credentialsId: "${TOMCAT_AZ_CRED}",
                    usernameVariable: 'TOMCAT_USER',
                    passwordVariable: 'TOMCAT_PASS'
                )]) {
                    sh """
                        curl -v --fail \
                          --upload-file ${WAR_FILE} \
                          "${TOMCAT_AZ_URL}/deploy?path=${DEPLOY_PATH}&update=true" \
                          --user "\${TOMCAT_USER}:\${TOMCAT_PASS}"
                    """
                }
                echo '==> Azure deployment complete.'
            }
        }

        // ---- Stage 7: Smoke Test ----
        stage('Smoke Test') {
            when {
                branch 'main'
            }
            steps {
                echo '==> Running smoke test on AWS deployment...'
                sh """
                    sleep 10
                    HTTP_CODE=\$(curl -s -o /dev/null -w "%{http_code}" \
                        http://your-aws-ec2-ip:8080${DEPLOY_PATH}/)
                    if [ "\$HTTP_CODE" != "200" ]; then
                        echo "Smoke test FAILED - HTTP \$HTTP_CODE"
                        exit 1
                    fi
                    echo "Smoke test PASSED - HTTP \$HTTP_CODE"
                """
            }
        }

    } // end stages

    // ---- Post Actions ----
    post {
        success {
            echo '==> Pipeline SUCCEEDED.'
            mail to: "${NOTIFY_EMAIL}",
                 subject: "SUCCESS: ${APP_NAME} Build #${env.BUILD_NUMBER}",
                 body: """
Build #${env.BUILD_NUMBER} completed successfully.
Branch : ${env.GIT_BRANCH}
Commit : ${env.GIT_COMMIT}
URL    : ${env.BUILD_URL}
"""
        }
        failure {
            echo '==> Pipeline FAILED.'
            mail to: "${NOTIFY_EMAIL}",
                 subject: "FAILED: ${APP_NAME} Build #${env.BUILD_NUMBER}",
                 body: """
Build #${env.BUILD_NUMBER} FAILED.
Branch : ${env.GIT_BRANCH}
Commit : ${env.GIT_COMMIT}
URL    : ${env.BUILD_URL}

Please check the Jenkins console for details.
"""
        }
        always {
            echo '==> Cleaning workspace...'
            cleanWs()
        }
    }

}

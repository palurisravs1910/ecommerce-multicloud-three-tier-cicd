// ============================================================
// Jenkinsfile - E-Commerce CI/CD Pipeline
// AWS Three-Tier Architecture
// Stack: GitHub → Jenkins → Maven → WAR → Tomcat
// ============================================================

pipeline {

    agent any

    // ---- Tool Declarations ----
    tools {
        maven 'maven'
        jdk   'jdk-21'
    }

    // ---- Pipeline-wide Environment Variables ----
    environment {
        APP_NAME        = 'ecommerce-app'
        WAR_FILE        = 'target/ecommerce-app.war'

        // Tomcat on AWS (App Tier)
        TOMCAT_AWS_URL  = 'http://16.112.90.159:8080/manager/text'
        TOMCAT_AWS_CRED = 'tomcat-aws-credentials'

        DEPLOY_PATH     = '/ecommerce-app'
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
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        // ---- Stage 3: Archive WAR ----
        stage('Archive Artifact') {
            steps {
                echo '==> Archiving WAR artifact...'
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
                echo "==> WAR archived: ${WAR_FILE}"
            }
        }

        // ---- Stage 4: Deploy to AWS Tomcat ----
        stage('Deploy to AWS') {
            steps {
                echo '==> Deploying to AWS Tomcat...'
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

        // ---- Stage 5: Smoke Test ----
        stage('Smoke Test') {
            steps {
                echo '==> Running smoke test...'
                sh """
                    sleep 10
                    HTTP_CODE=\$(curl -s -o /dev/null -w "%{http_code}" \
                        http://16.112.90.159:8080${DEPLOY_PATH}/)
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
        }
        failure {
            echo '==> Pipeline FAILED.'
        }
        always {
            echo '==> Cleaning workspace...'
            cleanWs()
        }
    }

}

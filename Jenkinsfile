// ============================================================
// Jenkinsfile - E-Commerce CI/CD Pipeline
// AWS Three-Tier Architecture
// Stack: GitHub → Jenkins → Maven → WAR → Tomcat (via ALB)
// ============================================================

pipeline {

    agent any

    tools {
        maven 'maven'
        jdk   'jdk-21'
    }

    environment {
        APP_NAME        = 'ecommerce-app'
        WAR_FILE        = 'target/ecommerce-app.war'
        TOMCAT_AWS_URL  = "${env.TOMCAT_URL ?: 'http://16.112.90.159:8080/manager/text'}"
        TOMCAT_AWS_CRED = 'tomcat-aws-credentials'
        DEPLOY_PATH     = '/ecommerce-app'
        ALB_DNS         = "${env.ALB_DNS ?: 'http://16.112.90.159:8080'}"
    }

    stages {

        // ---- Stage 1: Checkout ----
        stage('Checkout') {
            steps {
                echo '==> Checking out source code from GitHub...'
                checkout scm
                echo "==> Branch  : ${env.GIT_BRANCH}"
                echo "==> Commit  : ${env.GIT_COMMIT}"
                echo "==> Build # : ${env.BUILD_NUMBER}"
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

        // ---- Stage 3: Archive WAR Artifact ----
        stage('Archive Artifact') {
            steps {
                echo '==> Archiving WAR artifact...'
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
                echo "==> WAR size: ${sh(script: "du -sh ${WAR_FILE} | cut -f1", returnStdout: true).trim()}"
            }
        }

        // ---- Stage 4: Deploy to AWS Tomcat ----
        stage('Deploy to AWS') {
            steps {
                echo '==> Deploying WAR to AWS Tomcat via Manager API...'
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
                echo '==> Deployment to AWS Tomcat complete.'
            }
        }

        // ---- Stage 5: Smoke Test (via ALB) ----
        stage('Smoke Test') {
            steps {
                echo '==> Running smoke test via ALB DNS...'
                sh """
                    sleep 15
                    HTTP_CODE=\$(curl -s -o /dev/null -w "%{http_code}" \
                        ${ALB_DNS}${DEPLOY_PATH}/)
                    if [ "\$HTTP_CODE" != "200" ]; then
                        echo "Smoke test FAILED - HTTP \$HTTP_CODE"
                        exit 1
                    fi
                    echo "Smoke test PASSED - HTTP \$HTTP_CODE"
                """
            }
        }

    } // end stages

    post {
        success {
            echo "==> Pipeline SUCCEEDED. Build #${env.BUILD_NUMBER} deployed successfully."
        }
        failure {
            echo "==> Pipeline FAILED. Check console output for details."
        }
        always {
            echo '==> Cleaning workspace...'
            cleanWs()
        }
    }

}

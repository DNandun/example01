pipeline {
    agent any

    options {
        // Keep only the last 10 builds to save disk space
        buildDiscarder(logRotator(numToKeepStr: '10'))
        // Prevent simultaneous executions of the same pipeline
        disableConcurrentBuilds()
        // Fail pipeline if stuck for over 45 minutes
        timeout(time: 45, unit: 'MINUTES')
        // Enable timestamps in console log
        timestamps()
    }

    environment {
        APP_NAME          = 'student-management-system'
        DOCKER_IMAGE_NAME = 'student-management-system'
        DOCKER_TAG        = "${BUILD_NUMBER}"
        REPO_URL          = 'https://github.com/DNandun/example01.git'
        REGISTRY_CRED_ID  = 'docker-hub-credentials' // Configure in Jenkins Credentials if pushing to registry
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                echo "Fetching source code for ${env.APP_NAME} from ${env.REPO_URL}..."
                git branch: 'main', url: "${REPO_URL}"
            }
        }

        stage('Compile & Test') {
            steps {
                echo 'Building and running unit tests with Maven Wrapper...'
                script {
                    if (isUnix()) {
                        sh 'chmod +x mvnw'
                        sh './mvnw clean test'
                    } else {
                        bat 'mvnw.cmd clean test'
                    }
                }
            }
            post {
                always {
                    // Publish Maven JUnit test results in Jenkins UI
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('Package Application') {
            steps {
                echo 'Packaging application into executable JAR file...'
                script {
                    if (isUnix()) {
                        sh './mvnw package -DskipTests'
                    } else {
                        bat 'mvnw.cmd package -DskipTests'
                    }
                }
            }
            post {
                success {
                    // Archive the generated executable JAR file
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: false
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image: ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}..."
                script {
                    if (isUnix()) {
                        sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} -t ${DOCKER_IMAGE_NAME}:latest ."
                    } else {
                        bat "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} -t ${DOCKER_IMAGE_NAME}:latest ."
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying application and MySQL database using Docker Compose...'
                script {
                    if (isUnix()) {
                        sh 'docker compose down --remove-orphans || docker-compose down --remove-orphans || true'
                        sh 'docker compose up -d || docker-compose up -d'
                    } else {
                        bat 'docker compose down --remove-orphans 2>NUL || docker-compose down --remove-orphans 2>NUL || ver > NUL'
                        bat 'docker compose up -d || docker-compose up -d'
                    }
                }
            }
        }

        stage('Health Check') {
            steps {
                echo 'Verifying application health...'
                script {
                    if (isUnix()) {
                        sh '''
                            for i in $(seq 1 12); do
                                if curl -s -f http://localhost:8081/ > /dev/null; then
                                    echo "Application is UP and Healthy!"
                                    exit 0
                                fi
                                echo "Waiting for application to start... (attempt $i/12)"
                                sleep 5
                            done
                            echo "Application failed health check!"
                            exit 1
                        '''
                    } else {
                        bat '''
                            powershell -Command "$success = $false; for ($i=1; $i -le 12; $i++) { try { $res = Invoke-WebRequest -Uri 'http://localhost:8081/' -UseBasicParsing -TimeoutSec 3; if ($res.StatusCode -eq 200) { Write-Host 'Application is UP and Healthy!'; $success = $true; break } } catch { Write-Host 'Waiting for application...' }; Start-Sleep -Seconds 5 }; if (-not $success) { exit 1 }"
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution finished.'
            // Clean up workspace to maintain a clean build environment
            cleanWs(deleteDirs: true, notFailBuild: true)
        }
        success {
            echo "SUCCESS: ${env.APP_NAME} (Build #${env.BUILD_NUMBER}) built and deployed successfully!"
        }
        failure {
            echo "FAILURE: ${env.APP_NAME} (Build #${env.BUILD_NUMBER}) failed. Check console output for errors."
        }
    }
}


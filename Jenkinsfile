pipeline {
    agent {
        docker {
            image 'szhao95/private_repo:maven-3.9.7-eclipse-temurin-22-jammy'
            args '-v /root/.m2:/root/.m2' // Optional: for caching Maven dependencies
        }
    }
    environment {
        DOCKER_CREDENTIALS_ID = 'e431e977-3f3e-4222-ba23-4aa927879cd8'
        DOCKER_REPO = 'szhao95/private-repo'
        VERSION = "${env.BUILD_ID}" // Example versioning using the build ID
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/szhao2095/java_spring_jenkins_dropbox', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Code Coverage') {
            steps {
                script {
                    sh 'mvn jacoco:report'
                    def coverage = sh(script: "mvn jacoco:report | grep -oP 'Instruction missed count=\\\"(\\d+)\\\" covered count=\\\"(\\d+)\\\"' | awk '{sum += ($5 / ($3 + $5)) * 100} END {print sum}'", returnStdout: true).trim()
                    def coveragePercentage = coverage.toFloat()
                    if (coveragePercentage < 80.0) {
                        error "Code coverage is below 80%: ${coveragePercentage}%"
                    } else {
                        echo "Code coverage is sufficient: ${coveragePercentage}%"
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def app = docker.build("${DOCKER_REPO}:UserAuth-app-${VERSION}")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh "docker push ${DOCKER_REPO}:${VERSION}"
                    }
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
        success {
            echo "Pipeline completed successfully."
        }
        failure {
            echo "Pipeline failed."
        }
    }
}
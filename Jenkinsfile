// pipeline {
//     agent {
//         label 'java_spring'
//     }
//     stages {
//         stage('Ensure Docker Group Membership') {
//             steps {
//                 script {
//                     sh '''
//                         USER=$(id -un)
//                         # Ensure the Jenkins user is added to the Docker group
//                         gpasswd -a $USER docker
//                     '''
//                 }
//             }
//         }
//         stage('Ensure Docker is Running') {
//             steps {
//                 script {
//                     sh '''
//                         if ! pgrep -x "dockerd" > /dev/null; then
//                             echo "Docker daemon is not running. Attempting to start Docker..."
//                             nohup dockerd > dockerd.log 2>&1 &
//                             sleep 10

//                             # Wait until Docker daemon is ready
//                             for i in {1..10}; do
//                                 if docker info > /dev/null 2>&1; then
//                                     echo "Docker daemon is ready."
//                                     break
//                                 else
//                                     echo "Waiting for Docker daemon to be ready..."
//                                     sleep 5
//                                 fi
//                             done

//                             if ! docker info > /dev/null 2>&1; then
//                                 echo "Docker daemon failed to start."
//                                 cat dockerd.log
//                                 exit 1
//                             fi
//                         fi
//                     '''
//                 }
//             }
//         }
//         stage('Check Docker Status') {
//             steps {
//                 script {
//                     def isDockerReady = false
//                     for (int i = 0; i < 5; i++) {
//                         try {
//                             sh 'docker info'
//                             isDockerReady = true
//                             break
//                         } catch (Exception e) {
//                             echo "Docker daemon not ready, retrying..."
//                             sleep 5 // Wait 5 seconds before retrying
//                         }
//                     }
//                     if (!isDockerReady) {
//                         error "Docker daemon failed to start"
//                     }
//                 }
//             }
//         }
//         stage('Debug Docker Setup') {
//             steps {
//                 script {
//                     sh '''
//                         echo "### Checking Docker Installation ###"
//                         docker --version || echo "Docker CLI not found"
//                         echo "### Checking Docker Daemon Status ###"
//                         ps -ef | grep dockerd || echo "Docker daemon not running"
//                         echo "### Checking Docker Permissions ###"
//                         ls -l /var/run/docker.sock || echo "Docker socket not found or no permissions"
//                     '''
//                 }
//             }
//         }
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     def isBuildSuccessful = false
//                     for (int i = 0; i < 3; i++) {
//                         try {
//                             sh 'docker build -t test-docker-image .'
//                             isBuildSuccessful = true
//                             break
//                         } catch (Exception e) {
//                             echo "Docker build failed, retrying..."
//                             sleep 10 // Wait 10 seconds before retrying
//                         }
//                     }
//                     if (!isBuildSuccessful) {
//                         error "Docker build failed after retries"
//                     }
//                 }
//             }
//         }
//     }
//     post {
//         always {
//             cleanWs()
//         }
//         failure {
//             echo "Pipeline failed."
//         }
//     }
// }

pipeline {
    agent {
        label 'java_spring'
    }
    environment {
        DOCKER_CREDENTIALS_ID = 'e431e977-3f3e-4222-ba23-4aa927879cd8'
        DOCKER_REPO = 'szhao95/private-repo'
        APP_NAME = 'UserAuth'
        VERSION = "${env.BUILD_ID}" // Example versioning using the build ID
    }
    stages {
        stage('Ensure Docker Group Membership') {
            steps {
                script {
                    sh '''
                        USER=$(id -un)
                        # Ensure the Jenkins user is added to the Docker group
                        gpasswd -a $USER docker
                    '''
                }
            }
        }
        stage('Ensure Docker is Running') {
            steps {
                script {
                    sh '''
                        if ! pgrep -x "dockerd" > /dev/null; then
                            echo "Docker daemon is not running. Attempting to start Docker..."
                            nohup dockerd > dockerd.log 2>&1 &
                            sleep 10

                            # Wait until Docker daemon is ready
                            for i in {1..10}; do
                                if docker info > /dev/null 2>&1; then
                                    echo "Docker daemon is ready."
                                    break
                                else
                                    echo "Waiting for Docker daemon to be ready..."
                                    sleep 5
                                fi
                            done

                            if ! docker info > /dev/null 2>&1; then
                                echo "Docker daemon failed to start."
                                cat dockerd.log
                                exit 1
                            fi
                        fi
                    '''
                }
            }
        }
        stage('Check Docker Status') {
            steps {
                script {
                    def isDockerReady = false
                    for (int i = 0; i < 5; i++) {
                        try {
                            sh 'docker info'
                            isDockerReady = true
                            break
                        } catch (Exception e) {
                            echo "Docker daemon not ready, retrying..."
                            sleep 5 // Wait 5 seconds before retrying
                        }
                    }
                    if (!isDockerReady) {
                        error "Docker daemon failed to start"
                    }
                }
            }
        }
        stage('Debug Docker Setup') {
            steps {
                script {
                    sh '''
                        echo "### Checking Docker Installation ###"
                        docker --version || echo "Docker CLI not found"
                        echo "### Checking Docker Daemon Status ###"
                        ps -ef | grep dockerd || echo "Docker daemon not running"
                        echo "### Checking Docker Permissions ###"
                        ls -l /var/run/docker.sock || echo "Docker socket not found or no permissions"
                    '''
                }
            }
        }
        stage('Checkout') {
            steps {
                git url: 'https://github.com/szhao2095/java_spring_jenkins_dropbox', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean compile'
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
                    def coverage = sh(script: """
                        tail -n +2 target/site/jacoco/jacoco.csv | \
                        awk -F',' '{missed += \$4; covered += \$5} END {print (covered / (missed + covered)) * 100}'
                    """, returnStdout: true).trim()
                    if (coverage.isEmpty()) {
                        error "Code coverage calculation failed: No coverage data found."
                    }
                    def coveragePercentage = coverage.toFloat()
                    if (coveragePercentage < 70.0) {
                        error "Code coverage is below 70%: ${coveragePercentage}%"
                    } else {
                        echo "Code coverage is sufficient: ${coveragePercentage}%"
                    }
                }
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package -Dspring.profiles.active=prod -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    def app = docker.build("${DOCKER_REPO}:${APP_NAME}-${VERSION}")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKER_CREDENTIALS_ID) {
                        sh "docker push ${DOCKER_REPO}:${APP_NAME}-${VERSION}"
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
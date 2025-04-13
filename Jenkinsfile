pipeline {
    agent any

    tools {
        maven 'M2_HOME' // Nom de l'installation Maven dans "Global Tool Configuration"
    }

    environment {
        APP_ENV = "DEV"
        SONARQUBE = 'scanner' // Nom du serveur SonarQube configuré dans Jenkins
        registryCredentials = 'nexus' // ID des credentials pour Nexus dans Jenkins
        registry = '192.168.33.10:8083' // Adresse du registre Nexus
        imageName = 'my-app' // Nom de l’image Docker
    }

    options {
        wrap([$class: 'AnsiColorBuildWrapper', 'colorMapName': 'xterm'])
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }

    stages {

        stage('Checkout code') {
            steps {
                git branch: 'Haythem',
                    url: 'https://github.com/maramnaderi/devops.git',
                    credentialsId: 'github-pat'
            }
        }

        stage('Build Maven project') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('SonarQube analysis') {
            steps {
                script {
                    withSonarQubeEnv(SONARQUBE) {
                        sh """
                            mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet \
                            -Dsonar.host.url=http://172.26.59.33:9000
                        """
                    }
                }
            }
        }

        stage('Build Docker image') {
            steps {
                sh "docker-compose build"
            }
        }

        stage('Tag & Push Docker image to Nexus') {
            steps {
                script {
                    docker.withRegistry("http://${registry}", registryCredentials) {
                        sh """
                            docker tag ${imageName}:latest ${registry}/${imageName}:latest
                            docker push ${registry}/${imageName}:latest
                        """
                    }
                }
            }
        }

        stage('Run application with Docker Compose') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        failure {
            echo "💥 Build échoué, vérifie le stage précédent pour plus d'infos."
        }
        aborted {
            echo "🛑 Build annulé ou timeout dépassé."
        }
    }
}

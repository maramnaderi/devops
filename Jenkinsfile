pipeline {
    agent any

    tools {
        maven 'M2_HOME' // Assurez-vous que ce nom correspond bien dans Jenkins > Global Tool Configuration
    }

    environment {
        APP_ENV = "DEV"
        SONARQUBE = 'scanner' // Nom du serveur SonarQube dans Jenkins
        registryCredentials = 'nexus' // ID des credentials Nexus dans Jenkins
        registry = '192.168.33.10:8083' // Adresse de Nexus (sans HTTPS si ce n'est pas sécurisé)
        imageName = 'my-app' // Nom de l’image à builder et à pousser
    }

    options {
        ansiColor('xterm')
    timestamps()
    timeout(time: 10, unit: 'MINUTES') // pour éviter que le build reste bloqué trop longtemps
    }

    stages {

        stage('Checkout code') {
            steps {
                git branch: 'khairidinne',
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
                        sh '''
                            mvn sonar:sonar \
                            -Dsonar.projectKey=mon-projet \
                            -Dsonar.host.url=http://172.26.59.33:9000
                        '''
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
        always {
            echo "====== Pipeline terminé ======"
        }
        success {
            echo "✅ Pipeline exécuté avec succès"
        }
        failure {
            echo "❌ Échec de l'exécution du pipeline"
        }
    }
}

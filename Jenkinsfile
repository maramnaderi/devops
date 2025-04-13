pipeline {
    agent any

    tools {
        maven 'M2_HOME'
    }

    options {
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }

    environment {
        imageName = 'my-app'
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

        stage('Build Docker image') {
            steps {
                sh "docker-compose build"
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
            echo "💥 Build échoué, vérifie les étapes précédentes."
        }
        aborted {
            echo "🛑 Build annulé ou timeout dépassé."
        }
    }
}

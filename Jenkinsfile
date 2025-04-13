pipeline {
    agent any

    stages {
        stage('Vérification Git') {
            steps {
                echo 'Le dépôt a été cloné automatiquement via la configuration Jenkins.'
                sh 'ls -la'
            }
        }

        stage('Build Maven') {
            steps {
                echo '📦 Démarrage du build Maven...'
                sh 'mvn clean install'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline terminé avec succès.'
        }
        failure {
            echo '❌ Échec du pipeline.'
        }
    }
}

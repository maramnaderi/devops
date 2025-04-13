pipeline {
    agent any

    stages {
        stage('Vérification Git') {
            steps {
                echo 'Le dépôt a été cloné automatiquement via la configuration Jenkins.'
                sh 'ls -la'
            }
        }
    }

    post {
        success {
            echo '✅ Connexion à GitHub réussie.'
        }
        failure {
            echo '❌ Échec de la connexion au dépôt GitHub.'
        }
    }
}

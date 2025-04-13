pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('scanner') // Token SonarQube
    }

    stages {

        stage('Récupération du code') {
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/Haythem']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/maramnaderi/devops.git',
                            credentialsId: 'github-pat'
                        ]]
                    ])
                }
            }
        }

        stage('Compilation Maven') {
            steps {
                script {
                    try {
                        sh 'mvn clean compile'
                    } catch (Exception e) {
                        echo "❌ Erreur Compilation Maven : ${e}"
                        error "Échec de la compilation Maven"
                    }
                }
            }
        }

        stage('Tests Unitaires avec Mockito') {
            steps {
                script {
                    try {
                        sh 'mvn test'
                    } catch (Exception e) {
                        echo "❌ Erreur Tests Unitaires : ${e}"
                        error "Échec des tests unitaires"
                    }
                }
            }
        }

        stage('Génération du rapport JaCoCo') {
            steps {
                script {
                    try {
                        sh 'mvn jacoco:report'
                    } catch (Exception e) {
                        echo "❌ Erreur Rapport JaCoCo : ${e}"
                        error "Échec génération rapport JaCoCo"
                    }
                }
            }
        }

       

        stage('Packaging Maven (sans tests)') {
            steps {
                script {
                    try {
                        sh 'mvn clean package -DskipTests'
                    } catch (Exception e) {
                        echo "❌ Erreur Packaging Maven : ${e}"
                        error "Échec packaging Maven"
                    }
                }
            }
        }

         stage('Push Docker Image') {
            steps {
                script {
                    try {
                        sh "docker push nadianb/foyer:latest"
                        echo "✅ Image Docker poussée avec succès sur Docker Hub."
                    } catch (Exception e) {
                        echo "❌ Erreur Push Docker : ${e}"
                        error "Échec push Docker"
                    }
                }
            }
        }

       
    }

    post {
        always {
            emailext(
                from: 'haythem.raggad@esprit.tn',
                to: 'haythemraggad1920@gmail.com',
                subject: "Pipeline ${currentBuild.fullDisplayName} - Statut: ${currentBuild.currentResult}",
                body: """
                📊 Statut du build : ${currentBuild.currentResult}
                🔎 Projet SonarQube : projet-devops
                🔗 Logs Jenkins : ${env.BUILD_URL}
                """,
                recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                attachLog: true
            )
        }
        // ❌ Blocs de succès et d'échec supprimés
    }
}

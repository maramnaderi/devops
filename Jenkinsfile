pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'haythem2000/gestion-station-ski:latest'
        SONAR_TOKEN = credentials('scanner') // Token SonarQube, gardé pour référence
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
                        echo "Erreur lors de l'exécution de Maven : ${e}"
                        error "Échec dans l'étape de compilation Maven"
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
                        echo "Erreur lors des tests unitaires : ${e}"
                        error "Échec dans l'étape des tests unitaires"
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
                        echo "Erreur lors de la génération du rapport JaCoCo : ${e}"
                        error "Échec dans la génération du rapport JaCoCo"
                    }
                }
            }
        }  
        stage('Analyse SonarQube') {
            steps {
                script {
                    try {
                        sh '''
                            mvn sonar:sonar \
                            -Dsonar.projectKey=devops \
                            -Dsonar.host.url=http://172.28.240.88:9000 \
                            -Dsonar.login=$SONAR_TOKEN
                        '''
                    } catch (Exception e) {
                        echo "Erreur lors de l'analyse SonarQube : ${e}"
                        error "Échec dans l'étape d'analyse SonarQube"
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
                        echo "Erreur lors du packaging : ${e}"
                        error "Échec dans l'étape de packaging"
                    }
                }
            }
        }
                 stage('Déploiement sur Nexus') {
            steps {
                script {
                    try {
                        sh 'mvn deploy'
                    } catch (Exception e) {
                        echo "Erreur lors du déploiement sur Nexus : ${e}"
                        error "Échec dans l'étape de déploiement sur Nexus"
                    }
                }
            }
        }
         stage('Construction de l’image Docker') {
            steps {
                script {
                    try {
                        sh 'docker build -t haythem2000/gestion-station-ski:latest .'
                    } catch (Exception e) {
                        echo "Erreur lors de la construction de l'image Docker : ${e}"
                        error "Échec dans l'étape de construction de l'image Docker"
                    }
                }
            }
        }

        stage('Connexion à DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh "echo ${DOCKERHUB_PASSWORD} | docker login -u ${DOCKERHUB_USERNAME} --password-stdin"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    try {
                        sh "docker push haythem2000/gestion-station-ski:latest"
                        echo "✅ Image Docker poussée avec succès sur Docker Hub."
                    } catch (Exception e) {
                        echo "Erreur lors du push Docker : ${e}"
                        error "Échec dans l'étape de push Docker"
                    }
                }
            }
        }

stage('Déploiement avec Docker Compose') {
    steps {
        script {
            try {
sh '''
  docker-compose down -v --remove-orphans
  docker-compose pull
  docker-compose up -d --build --force-recreate
'''

            } catch (Exception e) {
                echo "Erreur lors du déploiement Docker Compose : ${e}"
                error "Échec dans le déploiement avec Docker Compose"
            }
        }
    }
}


    }
post {
        always {
            emailext(
                from: 'hlelikhairi04@gmail.com',
                to: 'khairidinne.hleli@esprit.tn',
                subject: "Pipeline ${currentBuild.fullDisplayName} - Statut: ${currentBuild.currentResult}",
                body: """
                Statut du build : ${currentBuild.currentResult}
                Projet analysé par SonarQube : projet-devops
                Logs disponibles : ${env.BUILD_URL}
                """,
                recipientProviders: [[$class: 'DevelopersRecipientProvider']],
                attachLog: true
            )
        }

        failure {
            emailext(
                from: 'hlelikhairi04@gmail.com',
                to: 'khairidinne.hleli@esprit.tn',
                subject: "❌ Échec du Pipeline ${currentBuild.fullDisplayName}",
                body: """
                Le pipeline a échoué à l'étape : ${currentBuild.fullDisplayName}.
                Logs : ${env.BUILD_URL}
                """,
                attachLog: true
            )
        }

        success {
            emailext(
                from: 'hlelikhairi04@gmail.com',
                to: 'khairidinne.hleli@esprit.tn',
                subject: "✅ Succès du Pipeline ${currentBuild.fullDisplayName}",
                body: """
                Le pipeline s'est terminé avec succès.
                Statut : ${currentBuild.currentResult}
                Logs : ${env.BUILD_URL}
                """,
                attachLog: true
            )
        }
    }

}


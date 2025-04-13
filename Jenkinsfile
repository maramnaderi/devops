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
                        branches: [[name: '*/Fadi']],
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
                            -Dsonar.host.url=http://172.23.202.74:9000 \
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
            // Use credentials from Jenkins
            withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'admin', passwordVariable: '123456')]) {
                try {
                    // Use the credentials in the deploy command
                    sh '''
                        echo "Deploying to Nexus..."
                        mvn deploy -Dusername=$NEXUS_USER -Dpassword=$NEXUS_PASS
                    '''
                } catch (Exception e) {
                    echo "Erreur lors du déploiement sur Nexus : ${e}"
                    error "Échec dans l'étape de déploiement sur Nexus"
                }
            }
        }
      }
     }

    }
}

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
                        sh 'mvn clean compile -e'
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
                        sh 'mvn test -e'
                    } catch (Exception e) {
                        echo "Erreur lors des tests unitaires : ${e}"
                        // Affichage des logs de test échoués
                        sh '''
                            echo "===== Logs de tests échoués ====="
                            if [ -d target/surefire-reports ]; then
                                cat target/surefire-reports/*.txt || true
                                cat target/surefire-reports/*.dumpstream || true
                            else
                                echo "Aucun rapport trouvé"
                            fi
                        '''
                        error "Échec dans l'étape des tests unitaires"
                    }
                }
            }
        }

        stage('Génération du rapport JaCoCo') {
            steps {
                script {
                    try {
                        sh 'mvn jacoco:report -e'
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
                            mvn sonar:sonar -e \
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
                        sh 'mvn clean package -DskipTests -e'
                    } catch (Exception e) {
                        echo "Erreur lors du packaging : ${e}"
                        error "Échec dans l'étape de packaging"
                    }
                }
            }
        }
    }
}

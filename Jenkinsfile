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
        
        stage('Ajout des dépendances nécessaires') {
            steps {
                script {
                    try {
                        // Sauvegarde du pom.xml original
                        sh 'cp pom.xml pom.xml.backup'
                        
                        // Ajouter la dépendance H2 au pom.xml
                        sh '''
                            sed -i '/<dependencies>/a \\t<dependency>\\n\\t\\t<groupId>com.h2database</groupId>\\n\\t\\t<artifactId>h2</artifactId>\\n\\t\\t<scope>test</scope>\\n\\t</dependency>' pom.xml
                        '''
                        echo "Dépendance H2 ajoutée au pom.xml"
                        
                        // Ajouter le plugin JaCoCo au pom.xml
                        sh '''
                            sed -i '/<\\/plugins>/i \\t\\t<plugin>\\n\\t\\t\\t<groupId>org.jacoco</groupId>\\n\\t\\t\\t<artifactId>jacoco-maven-plugin</artifactId>\\n\\t\\t\\t<version>0.8.7</version>\\n\\t\\t\\t<executions>\\n\\t\\t\\t\\t<execution>\\n\\t\\t\\t\\t\\t<goals>\\n\\t\\t\\t\\t\\t\\t<goal>prepare-agent</goal>\\n\\t\\t\\t\\t\\t</goals>\\n\\t\\t\\t\\t</execution>\\n\\t\\t\\t\\t<execution>\\n\\t\\t\\t\\t\\t<id>report</id>\\n\\t\\t\\t\\t\\t<phase>prepare-package</phase>\\n\\t\\t\\t\\t\\t<goals>\\n\\t\\t\\t\\t\\t\\t<goal>report</goal>\\n\\t\\t\\t\\t\\t</goals>\\n\\t\\t\\t\\t</execution>\\n\\t\\t\\t</executions>\\n\\t\\t</plugin>' pom.xml
                        '''
                        echo "Plugin JaCoCo ajouté au pom.xml"
                        
                        // Vérifier que le pom.xml est toujours valide
                        sh 'mvn help:effective-pom -Doutput=/dev/null'
                        
                    } catch (Exception e) {
                        echo "Erreur lors de la modification du pom.xml: ${e}"
                        // Restaurer le pom.xml original en cas d'erreur
                        sh 'mv pom.xml.backup pom.xml'
                        echo "Le pom.xml original a été restauré"
                    }
                }
            }
        }
        
        stage('Configuration de test') {
            steps {
                script {
                    try {
                        // Créer un fichier application-test.properties pour les tests
                        sh '''
                            mkdir -p src/test/resources
                            cat > src/test/resources/application-test.properties << EOF
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
EOF
                        '''
                        echo "Fichier application-test.properties créé pour les tests"
                    } catch (Exception e) {
                        echo "Avertissement: Impossible de créer le fichier de configuration de test: ${e}"
                        // Continuer malgré l'erreur
                    }
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
                        // Exécuter les tests avec le profil de test
                        sh 'mvn test -Dspring.profiles.active=test -e'
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
                        echo "Avertissement lors de la génération du rapport JaCoCo : ${e}"
                        echo "Continuons avec les étapes suivantes..."
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
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline exécuté avec succès!'
        }
        failure {
            echo 'La pipeline a échoué. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}

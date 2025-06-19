# ✅ Exemple de démarrage du JAR Spring Boot

# Assurez-vous que `secrets.properties` et `echange-af-api.jar` sont dans le même dossier

nohup java \
  -Dserver.port=8073 \
  -Dspring.config.location=classpath:/,file:./secrets.properties \
  -jar echange-af-api.jar >> echange.log & tail -f echange.log

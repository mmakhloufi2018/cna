# ✅ Exemple de démarrage du JAR Spring Boot

# Assurez-vous que `secrets.properties` et `echange-af-pp.jar` sont dans le même dossier

nohup java \
  -Dserver.port=8880 \
  -Dspring.config.location=classpath:/,file:./secrets.properties \
  -jar echange-af-pp.jar >> echange.log & tail -f echange.log

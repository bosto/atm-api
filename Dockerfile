FROM java:8-jre
MAINTAINER Bosto <lixianhaobosto@gmail.com>

ADD ./target/atm-api.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/atm-api.jar"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8001/actuator/health || exit 1

EXPOSE 8001
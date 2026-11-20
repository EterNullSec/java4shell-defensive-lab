FROM eclipse-temurin:21.0.13_9-jdk
WORKDIR /app
COPY LabProbe.java /tmp/LabProbe.java
RUN javac -d /app /tmp/LabProbe.java && rm /tmp/LabProbe.java \
    && useradd --system --uid 10001 --no-create-home --shell /usr/sbin/nologin lab
USER 10001:10001
ENTRYPOINT ["java", "-cp", "/app", "LabProbe"]

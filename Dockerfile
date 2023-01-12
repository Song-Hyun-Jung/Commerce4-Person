FROM gradle:7.1.0-jdk11
USER root

RUN useradd -ms /bin/bash wasadm
RUN rm -rf /app
RUN mkdir -p /app

RUN chown -R wasadm:wasadm /app

RUN cd /app
RUN git clone https://github.com/Song-Hyun-Jung/Commerce4-Common.git /app/Commerce4-Common
RUN git clone https://github.com/Song-Hyun-Jung/Commerce4-Person.git /app/Commerce4-Person

WORKDIR /app/Commerce4-Person

RUN cd /app/Commerce4-Person

RUN chmod -R 755 /app/*

RUN gradle build

CMD ["java", "-jar", "/app/Commerce4-Person/build/libs/Commerce4-Person-0.0.1-SNAPSHOT.jar"]



FROM gradle:7.4.2-jdk11 AS BUILD
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle server:installDist

FROM openjdk:11
EXPOSE 8080
RUN mkdir "/app"
RUN apt-get update
RUN apt -y install python3-pygments python3-autopep8 python3-pillow
COPY --from=build /home/gradle/src /app/
RUN mkdir -p /app/generation_data
ENV GENERATION_DIR="/app/generation_data"
ENTRYPOINT ["/app/server/build/install/server/bin/server"]

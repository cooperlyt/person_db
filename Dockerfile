FROM ibm-semeru-runtimes:open-21-jre
ARG JAR_FILE
ENV TZ=Asia/Shanghai \
    DEBIAN_FRONTEND=noninteractive
RUN apt-get -y update && apt-get -y upgrade \
    && apt-get -y install --no-install-recommends \
    tzdata \
    && ln -fs /usr/share/zoneinfo/${TZ} /etc/localtime \
    && echo ${TZ} > /etc/timezone \
    && dpkg-reconfigure --frontend noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*  \
    && mkdir -p /usr/local/app \
    && mkdir -p /opt/config
ADD ${JAR_FILE} /usr/local/app/app.jar


ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/local/app/app.jar"]


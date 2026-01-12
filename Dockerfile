FROM amazoncorretto:17-alpine
WORKDIR /app
# Copia a pasta lib contendo o mysql-connector e o json.jar
COPY lib/ ./lib/
COPY src/ ./src/
COPY config/ ./config/
RUN mkdir out
# Compila referenciando a pasta lib
RUN javac -cp "lib/*" -d out src/br/com/ddb/common/*.java src/br/com/ddb/middleware/*.java
CMD ["java", "-cp", "out:lib/*", "br.com.ddb.middleware.DDBNode"]
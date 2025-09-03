# Estágio 1: Build com Maven
# Usa uma imagem oficial do Maven com Java 21 para compilar o projeto.
FROM maven:3.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do contêiner.
WORKDIR /app

# Copia o pom.xml e baixa as dependências.
# Isso aproveita o cache do Docker se as dependências não mudarem.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia todo o código-fonte do projeto.
COPY src ./src

# Compila o projeto, gera o pacote .jar e pula os testes.
RUN mvn clean package -DskipTests


# Estágio 2: Runtime
# Usa uma imagem Java Runtime (JRE) muito menor, apenas para executar.
FROM eclipse-temurin:21-jre-jammy

# Define o diretório de trabalho.
WORKDIR /app

# Expõe a porta que a aplicação Spring Boot usa.
EXPOSE 8080

# Copia o arquivo .jar construído no estágio anterior para a imagem final.
COPY --from=builder /app/target/*.jar app.jar

# Comando para iniciar a aplicação quando o contêiner for executado.
ENTRYPOINT ["java", "-jar", "app.jar"]
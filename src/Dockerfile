# --- Estágio 1: Build (Construção) ---
# Usamos uma imagem que tem o Java 21 (JDK) e o Maven (via mvnw)
FROM eclipse-temurin:21-jdk-jammy AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos de build primeiro para otimizar o cache
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Dá permissão de execução para o script mvnw
RUN chmod +x mvnw

# Copia o resto do código-fonte
COPY src src

# Executa o comando de build (igual ao que íamos usar no Render)
# Isso vai compilar o código e gerar o .jar
RUN ./mvnw clean package -DskipTests


# --- Estágio 2: Run (Execução) ---
# Usamos uma imagem "limpa" que só tem o Java para rodar (JRE)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia o .jar que foi gerado no Estágio 1
# Note que o nome do .jar precisa estar correto aqui
COPY --from=builder /app/target/quiz-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta que o Spring Boot usa (8080)
EXPOSE 8080

# Comando final para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
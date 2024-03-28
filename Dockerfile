FROM amazoncorretto:17

WORKDIR /backend

COPY target/MedicalClinicCompanyManager-0.0.1-SNAPSHOT.jar /backend/backend.jar

EXPOSE 8080

CMD ["java", "-jar", "backend.jar"]
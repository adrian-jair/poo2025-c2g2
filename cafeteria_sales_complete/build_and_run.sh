#!/bin/bash
set -e
mkdir -p out
echo "Compilando..."
javac -d out $(find src -name "*.java")
echo "Creando JAR ejecutable cafeteria_sales.jar..."
jar cfe cafeteria_sales.jar app.Main -C out .
echo "Listo. Ejecuta: java -jar cafeteria_sales.jar"

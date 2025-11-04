\
    @echo off
    if not exist out mkdir out
    echo Compilando...
    for /R src %%f in (*.java) do @(echo Compiling %%f & javac -d out "%%f")
    echo Creando JAR ejecutable cafeteria_sales.jar...
    jar cfe cafeteria_sales.jar app.Main -C out .
    echo Listo. Ejecuta: java -jar cafeteria_sales.jar

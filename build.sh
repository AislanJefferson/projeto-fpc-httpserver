#!/bin/bash
build_dir="build/"
jar_filename="server.jar"
if [ -d $build_dir ]; then
    rm -rf $build_dir server.jar
fi
mkdir $build_dir
javac -d ./$build_dir fpcc/Main.java
cd $build_dir
jar cvfe ../$jar_filename fpcc.Main *
cd ../
rm -rf $build_dir
if [ -e $jar_filename ]; then
    echo "Executavel construido com sucesso"
else
    echo "Erro ao construir o executavel"
fi

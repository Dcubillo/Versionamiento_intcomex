@echo off
echo "Uniendo Archivos"
cd %HOMEPATH%\Nimbus_Descargas
copy /b *.csv ProductsMerge.csv
echo hecho 
exit
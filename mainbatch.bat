start batch1.bat
ping 127.0.0.1 -n 6 > nul
start batch2.bat
ping 127.0.0.1 -n 2 > nul
start ExtractPort
ping 127.0.0.1 -n 3 > nul
git add .
git commit -m port.txt
git push origin
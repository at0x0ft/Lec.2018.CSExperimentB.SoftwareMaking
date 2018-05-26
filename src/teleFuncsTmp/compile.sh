#! bin/bash
javac main/*.java server/*.java client/*.java

java main/LoveLetter

rm main/*.class server/*.class client/*.class

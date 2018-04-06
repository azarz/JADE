# JADE - JAva Driving Experience

Contributors : Amaury Zarzelli, Antoine Roca, Antoine Moutou, Augustin Gagnon, Eva Chen-Yen-Su, Laure Le Breton, Mariem Moukhlissi

## Prérequis
Oracle JDK version 8.
### Installation sur linux
Lancer les commandes suivantes pour installer le jdk oracle sur une distribution linux dérivée de Debian (comme Ubuntu par exemple)
```sh
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```

## Comment lancer ?

Se placer dans un dossier, lancer la commande 
```sh
git clone https://gitlab.com/azarzelli/JADE.git
```

Une fois le téléchargement terminé lancer
```sh
cd JADE
git checkout demo
```
puis pour lancer l'application
```sh
java -jar JADE-0.0.1-SNAPHOT.jar
```

Puis se diriger dans le dossier RGE pour lancer MAIN_FILE.xml
